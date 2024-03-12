package ui;

import data.StatisticDao;
import model.CategoryStatistic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsTable extends AbstractEntityTableModel<CategoryStatistic> {
    private static final I18N I18N = new I18N(StatisticsTable.class);

    private static final List<Column<?, CategoryStatistic>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("category"), String.class, CategoryStatistic::getCategoryName),
            Column.readOnly(I18N.getString("transactions"), Integer.class, CategoryStatistic::getTransactionsCounter),
            Column.readOnly(I18N.getString("income"), BigDecimal.class, CategoryStatistic::getIncome),
            Column.readOnly(I18N.getString("expenses"), BigDecimal.class, CategoryStatistic::getExpenses),
            Column.readOnly(I18N.getString("totalIncome"), BigDecimal.class, CategoryStatistic::getPercentageInc),
            Column.readOnly(I18N.getString("totalSpending"), BigDecimal.class, CategoryStatistic::getPercentageSpend),
            Column.readOnly(I18N.getString("sum"), BigDecimal.class, CategoryStatistic::getSum)
    );

    private List<CategoryStatistic> statistics;
    private final StatisticDao statisticDao;
    private Date dateFrom;
    private Date dateTo;

    protected StatisticsTable(StatisticDao statisticDao) {
        super(COLUMNS);
        this.statisticDao = statisticDao;
        statistics = new ArrayList<>();
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void update(){
        statistics = statisticDao.setAll(dateFrom, dateTo);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return statistics.size();
    }

    @Override
    protected CategoryStatistic getEntity(int rowIndex) {
        return statistics.get(rowIndex);
    }
}
