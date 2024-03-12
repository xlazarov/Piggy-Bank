
import data.CategoryDao;
import data.DataAccessException;
import data.TransactionDao;
import enums.TransactionType;
import model.Category;
import model.Transaction;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


final class TransactionDaoTest {
    private static EmbeddedDataSource dataSource;
    private CategoryDao categoryDao;
    private TransactionDao transactionDao;


    @BeforeAll
    static void initTestDataSource() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("memory:piggy-bank-test");
        dataSource.setCreateDatabase("create");
    }

    @BeforeEach
    void createTransactionDao() throws SQLException {
        categoryDao = new CategoryDao(dataSource);
        transactionDao = new TransactionDao(dataSource);
        try (var connection = dataSource.getConnection(); var st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM APP.TRANSACTIONS");
        }
    }

    @AfterEach
    void cleanUp() {
        transactionDao.dropTable();
        categoryDao.dropTable();
    }


    @Test
    void createTransaction() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,new Date(2021,1,1),"test", TransactionType.INCOME);
        categoryDao.create(food);
        transactionDao.create(bread);

        assertThat(bread.getId())
                .isNotNull();
        assertThat(transactionDao.findById(bread.getId()))
                .isNotSameAs(bread)
                .isEqualToComparingFieldByField(bread);
    }

    @Test
    void createTransactionWithExistingId() {
        var sales = new Transaction("Test", new BigDecimal(100), new Category("Food", Color.red),new Date(2021,1,1),"test", TransactionType.SPENDING);
        sales.setId(123L);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionDao.create(sales))
                .withMessage("Transaction already has ID: " + sales);
    }

    @Test
    void createTransactionWithException() {
        var sqlException = new SQLException();
        TransactionDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.create(bread))
                .withMessage("Failed to store transaction " + bread)
                .withCause(sqlException);
    }

    @Test
    void findAllEmpty() {
        assertThat(transactionDao.findAll())
                .isEmpty();
    }

    @Test
    void findAll() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        var milk = new Transaction("Test", new BigDecimal(120), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        var mustard = new Transaction("Test", new BigDecimal(5000), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);

        categoryDao.create(food);
        transactionDao.create(bread);
        transactionDao.create(milk);
        transactionDao.create(mustard);

        assertThat(transactionDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(bread, milk, mustard);
    }

    @Test
    void findAllWithException() {
        var sqlException = new SQLException();
        TransactionDao failingDao = createFailingDao(sqlException);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(failingDao::findAll)
                .withMessage("Failed to load all transactions")
                .withCause(sqlException);
    }

    @Test
    void delete() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        var milk = new Transaction("Test", new BigDecimal(120), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);

        categoryDao.create(food);
        transactionDao.create(bread);
        transactionDao.create(milk);
        transactionDao.delete(bread);

        assertThat(transactionDao.findAll())
                .usingFieldByFieldElementComparator()
                .containsExactly(milk);
    }

    @Test
    void deleteWithNullId() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionDao.delete(bread))
                .withMessage("Transaction has null ID" + bread);
    }

    @Test
    void deleteNonExisting() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        bread.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> transactionDao.delete(bread))
                .withMessage("Failed to delete non-existing transaction: " + bread);
    }

    @Test
    void deleteWithException() {
        var sqlException = new SQLException();
        TransactionDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        bread.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.delete(bread))
                .withMessage("Failed to delete transaction " + bread)
                .withCause(sqlException);
    }

    @Test
    void update() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        var anotherBread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);

        categoryDao.create(food);
        transactionDao.create(bread);
        transactionDao.create(anotherBread);

        bread.setName("newBread");
        bread.setAmount(new BigDecimal(150));

        transactionDao.update(bread);

        assertThat(transactionDao.findById(bread.getId()))
                .isEqualToComparingFieldByField(bread);
        assertThat(transactionDao.findById(anotherBread.getId()))
                .isEqualToComparingFieldByField(anotherBread);
    }

    @Test
    void updateWithNullId() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionDao.update(bread))
                .withMessage("Transaction has null ID: " + bread);
    }

    @Test
    void updateNonExisting() {
        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        bread.setId(123L);

        categoryDao.create(food);
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> transactionDao.update(bread))
                .withMessage("Failed to update non-existing transaction: " + bread);
    }

    @Test
    void updateWithException() {
        var sqlException = new SQLException();
        TransactionDao failingDao = createFailingDao(sqlException);

        var food = new Category("Food", Color.red);
        var bread = new Transaction("Test", new BigDecimal(100), food,
                new Date(2021,1,1),"test", TransactionType.SPENDING);
        bread.setId(123L);

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> failingDao.update(bread))
                .withMessage("Failed to update transaction " + bread)
                .withCause(sqlException);
    }

    private TransactionDao createFailingDao(Throwable exceptionToBeThrown) {
        try {
            var dataSource = mock(DataSource.class);
            when(dataSource.getConnection()).thenAnswer(i -> TransactionDaoTest.dataSource.getConnection());
            var transactionDao = new TransactionDao(dataSource);
            when(dataSource.getConnection()).thenThrow(exceptionToBeThrown);
            return transactionDao;
        } catch (SQLException ex) {
            throw new RuntimeException("Mock configuration failed", ex);
        }
    }
}
