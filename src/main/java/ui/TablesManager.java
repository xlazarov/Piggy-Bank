package ui;

import data.CategoryDao;
import data.StatisticDao;
import data.TransactionDao;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public class TablesManager {
    private final StatisticsTable statisticsTableModel;
    private final StatisticsBalanceTable statisticsBalanceTableModel;
    private final CategoriesTable categoriesTableModel;
    private final TransactionsTable transactionsTableModel;

    private final JTable statisticsJTable;
    private final JTable statisticsBalanceJTable;
    private final JTable categoriesJTable;
    private final JTable transactionsJTable;

    public TablesManager(CategoryDao categoryDao, TransactionDao transactionDao, StatisticDao statisticDao){
        statisticsTableModel = new StatisticsTable(statisticDao);
        statisticsBalanceTableModel = new StatisticsBalanceTable(statisticDao);
        categoriesTableModel = new CategoriesTable(categoryDao);
        transactionsTableModel = new TransactionsTable(transactionDao, categoriesTableModel);

        statisticsJTable = createJTable(statisticsTableModel);
        statisticsJTable.setCellSelectionEnabled(false);
        statisticsBalanceJTable = createStatisticsBalanceJTable();
        categoriesJTable = createJTable(categoriesTableModel);
        transactionsJTable = createJTable(transactionsTableModel);
        disableSorters();

        setTablesColumnsAlign();
        setRenderers();
    }

    public JTable getStatJTable() {
        return statisticsJTable;
    }

    public JTable getStatBalJTable() {
        return statisticsBalanceJTable;
    }

    public JTable getCatJTable() {
        return categoriesJTable;
    }

    public JTable getTranJTable() {
        return transactionsJTable;
    }

    public StatisticsTable getStatTableModel() {
        return statisticsTableModel;
    }

    public StatisticsBalanceTable getStatBalTableModel() {
        return statisticsBalanceTableModel;
    }

    public CategoriesTable getCatTableModel() {
        return categoriesTableModel;
    }

    public TransactionsTable getTranTableModel() {
        return transactionsTableModel;
    }

    private void disableSorters(){
        transactionsJTable.setRowSorter(null);
        categoriesJTable.setRowSorter(null);
        statisticsBalanceJTable.setRowSorter(null);
        statisticsJTable.setRowSorter(null);
    }

    private void setRenderers(){
        categoriesJTable.setDefaultRenderer(Color.class, new CellColorRenderer());
        transactionsJTable.setDefaultRenderer(Color.class, new CellColorRenderer());
    }

    private JTable createJTable(Object o){
        var table = new JTable((TableModel) o);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(20);
        return table;
    }

    private void setTablesColumnsAlign(){
        setColumnAlign(transactionsJTable, JLabel.LEFT, new int[]{1});
        setColumnAlign(statisticsBalanceJTable, JLabel.RIGHT, new int[]{1});
        setColumnAlign(statisticsJTable, JLabel.LEFT, new int[]{1, 2, 3, 4, 5, 6});
    }

    private void setColumnAlign(JTable table, int option, int[] columns){
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(option);
        for (int column : columns) {
            table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
        }
    }

    private JTable createStatisticsBalanceJTable(){
        JTable table = new JTable(statisticsBalanceTableModel);
        table.setTableHeader(null);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        table.setFont(table.getFont().deriveFont(Font.BOLD));
        table.setCellSelectionEnabled(false);
        return table;
    }

    public void addListSelectionListenerToTables(ToolBar toolBar){
        statisticsJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
        transactionsJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
        categoriesJTable.getSelectionModel().addListSelectionListener(toolBar::rowSelectionChanged);
    }
}
