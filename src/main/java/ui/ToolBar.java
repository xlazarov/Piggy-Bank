package ui;

import enums.TableType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class ToolBar extends JToolBar {
    private final AddAction addAction;
    private final DeleteAction deleteAction;
    private final EditAction editAction;
    private final FilterAction filterAction;
    private int selectedTabIndex = 0;

    public ToolBar(JFrame frame, TablesManager tablesManager){
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MessageDialog messageDialog = new MessageDialog(frame);
        this.addAction = new AddAction(frame, tablesManager, messageDialog);
        this.deleteAction = new DeleteAction(tablesManager, messageDialog);
        this.editAction = new EditAction(frame, tablesManager, messageDialog);
        this.filterAction = new FilterAction(tablesManager, messageDialog);
        setToolBar();
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        addAction.updateSelectedTabIndex(selectedTabIndex);
        deleteAction.updateSelectedTabIndex(selectedTabIndex);
        editAction.updateSelectedTabIndex(selectedTabIndex);
        filterAction.updateSelectedTabIndex(selectedTabIndex);
    }

    private void setToolBar(){
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.add(addAction);
        this.add(deleteAction);
        this.add(editAction);
        this.add(filterAction.getFilterPanel());
        this.setFloatable(false);
        this.setVisible(true);
    }

    public void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();

        deleteAction.setEnabled(
                selectionModel.getSelectedItemsCount() != 0
                && selectedTabIndex != TableType.STATISTICS.ordinal()
        );

        editAction.setEnabled(
                selectionModel.getSelectedItemsCount() == 1
                && selectedTabIndex != TableType.STATISTICS.ordinal()
        );
    }
}
