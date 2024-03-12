package data;

import enums.TransactionType;
import model.Category;
import model.CategoryStatistic;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticDao {
    private final DataSource dataSource;

    public StatisticDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<CategoryStatistic> setAll(Date from, Date to){
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, \"NAME\", COLOR FROM CATEGORIES")) {
            List<CategoryStatistic> categoryStatistics = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(rs.getString("NAME"), Color.decode(rs.getString("COLOR")));
                    category.setId(rs.getLong("ID"));
                    CategoryStatistic categoryStatistic = new CategoryStatistic(category);
                    categoryStatistic.setTransactionsCounter(getNumberOfTransactions(category, from, to));
                    categoryStatistic.setExpenses(getIncomeExpense(category, TransactionType.SPENDING, from, to));
                    categoryStatistic.setIncome(getIncomeExpense(category, TransactionType.INCOME, from, to));
                    categoryStatistic.setSum(categoryStatistic.getIncome().subtract(categoryStatistic.getExpenses()) );
                    setPercentageIncomeAndExpense(categoryStatistic, from, to);
                    categoryStatistics.add(categoryStatistic);
                }
                return categoryStatistics;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to set all CategoryStatistics", ex);
        }
    }

    private void setPercentageIncomeAndExpense(CategoryStatistic categoryStatistic, Date from, Date to) {
        try {
            categoryStatistic.setPercentageInc(categoryStatistic.getIncome()
                    .divide(getTotalIncomeExpense(TransactionType.INCOME, from, to),2,RoundingMode.HALF_UP)
                    );
        } catch (ArithmeticException e){
            categoryStatistic.setPercentageInc(new BigDecimal(0));
        }
        try {
            categoryStatistic.setPercentageSpend(categoryStatistic.getExpenses()
                    .divide(getTotalIncomeExpense(TransactionType.SPENDING, from, to),2,RoundingMode.HALF_UP)
                    );
        } catch (ArithmeticException e){
            categoryStatistic.setPercentageSpend(new BigDecimal(0));
        }
    }

    public int getNumberOfTransactions(Category category, Date from, Date to){
        int number = 0;
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT CATEGORY_ID, CREATION_DATE FROM TRANSACTIONS WHERE CATEGORY_ID = ?")) {
            st.setLong(1, category.getId());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Date creationDate = new java.util.Date(rs.getDate("CREATION_DATE").getTime());
                    if (from != null && to != null && !(creationDate.after(from) && creationDate.before(to))){
                        continue;
                    }
                    ++number;
                }
            }
            return number;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all transactions", ex);
        }
    }

    public BigDecimal getIncomeExpense(Category category, TransactionType transactionType, Date from, Date to){
       BigDecimal amount = new BigDecimal(0);
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT CATEGORY_ID, \"TYPE\", CREATION_DATE, AMOUNT FROM TRANSACTIONS WHERE CATEGORY_ID = ? AND \"TYPE\" = ?"
             )){
            st.setLong(1, category.getId());
            st.setString(2, transactionType.name());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Date creationDate = new java.util.Date(rs.getDate("CREATION_DATE").getTime());
                    if (from != null && to != null && !(creationDate.after(from) && creationDate.before(to))){
                        continue;
                    }
                    amount = amount.add(BigDecimal.valueOf(rs.getDouble("AMOUNT")));
                }
            }
            return amount.setScale(2,RoundingMode.HALF_UP);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get income or expense " + category, ex);
        }
    }

    public BigDecimal getTotalIncomeExpense(TransactionType transactionType, Date from, Date to){
        BigDecimal amount = new BigDecimal(0);
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT \"TYPE\", CREATION_DATE, AMOUNT FROM TRANSACTIONS WHERE \"TYPE\" = ?"
             )){
            st.setString(1, transactionType.name());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Date creationDate = new java.util.Date(rs.getDate("CREATION_DATE").getTime());
                    if (from != null && to != null && !(creationDate.after(from) && creationDate.before(to))){
                        continue;
                    }
                    amount = amount.add(BigDecimal.valueOf(rs.getDouble("AMOUNT")));
                }
            }
            return amount;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get total income or expense " + ex);
        }
    }

    public BigDecimal getBalance(Date from, Date to){
        BigDecimal balance = new BigDecimal(0);
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT CREATION_DATE, \"TYPE\", AMOUNT FROM TRANSACTIONS")) {
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Date creationDate = new java.util.Date(rs.getDate("CREATION_DATE").getTime());
                    if (from != null && to != null && !(creationDate.after(from) && creationDate.before(to))){
                        continue;
                    }
                    TransactionType type = TransactionType.valueOf(rs.getString("TYPE"));
                    if (type == TransactionType.INCOME){
                        balance = balance.add(BigDecimal.valueOf(rs.getDouble("AMOUNT")));
                    } else {
                        balance = balance.subtract(BigDecimal.valueOf(rs.getDouble("AMOUNT")));
                    }
                }
            }
            return balance.setScale(2,RoundingMode.HALF_UP);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all transactions", ex);
        }
    }
}
