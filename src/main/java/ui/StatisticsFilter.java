package ui;

public class StatisticsFilter {
    private final FilterPanel filterPanel;
    private final TablesManager tablesManager;

    public StatisticsFilter(TablesManager tablesManager, FilterPanel filterPanel){
        this.filterPanel = filterPanel;
        this.tablesManager = tablesManager;
        filterTable();
    }

    public void filterTable(){
        this.tablesManager.getStatTableModel().setDateFrom(this.filterPanel.getDateFrom());
        this.tablesManager.getStatTableModel().setDateTo(this.filterPanel.getDateTo());
        tablesManager.getStatTableModel().update();

        this.tablesManager.getStatBalTableModel().setDateFrom(this.filterPanel.getDateFrom());
        this.tablesManager.getStatBalTableModel().setDateTo(this.filterPanel.getDateTo());
        tablesManager.getStatBalTableModel().update();
    }


}
