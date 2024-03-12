package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class TabbedPane extends JTabbedPane {
    private final TablesManager tablesManager;
    private final ToolBar toolBar;
    private static final I18N I18N = new I18N(TabbedPane.class);

    public TabbedPane(TablesManager tablesManager, ToolBar toolBar){
        this.tablesManager = tablesManager;
        this.toolBar = toolBar;
        addJTablesToPane();
        this.addChangeListener(this::tabChanged);
    }

    private void clearSelections(){
        tablesManager.getStatJTable().clearSelection();
        tablesManager.getTranJTable().clearSelection();
        tablesManager.getCatJTable().clearSelection();
    }

    private void tabChanged(ChangeEvent changeEvent){
        clearSelections();
        toolBar.updateSelectedTabIndex(this.getSelectedIndex());
    }

    private JPanel createStatisticsJPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add( tablesManager.getStatJTable().getTableHeader());
        panel.add( tablesManager.getStatJTable());
        panel.add(tablesManager.getStatBalJTable());
        return panel;
    }

    private void addJTablesToPane(){
        this.add(I18N.getString("statistics"), new JScrollPane(createStatisticsJPanel()));
        this.add(I18N.getString("transactions"), new JScrollPane(tablesManager.getTranJTable()));
        this.add(I18N.getString("categories"), new JScrollPane(tablesManager.getCatJTable()));
    }
}
