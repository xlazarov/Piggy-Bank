package ui;

import data.StatisticDao;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.Date;

public class StatisticsBalanceTable extends AbstractTableModel {

    private BigDecimal balance;
    private final StatisticDao statisticDao;
    private Date dateFrom;
    private Date dateTo;
    private static final I18N I18N = new I18N(StatisticsBalanceTable.class);

    public StatisticsBalanceTable(StatisticDao statisticDao){
        this.statisticDao = statisticDao;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void update(){
        balance = statisticDao.getBalance(dateFrom, dateTo);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return I18N.getString("balance");
            case 1:
                return balance;
            default:
                return null;    // TODO ADD SOME EXCEPTION HERE
        }
    }
}