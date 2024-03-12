package ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteTransaction {
    private final TablesManager tablesManager;
    private final MessageDialog messageDialog;
    private static final I18N I18N = new I18N(DeleteTransaction.class);

    public DeleteTransaction(TablesManager tablesManager, MessageDialog messageDialog){
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }

    public void delete() {
        String message = I18N.getString("message");
        message += messageDialog.createItemsString(tablesManager.getTranJTable());
        if (!messageDialog.showConfirmMessage(message, I18N.getString("title"))){
            return;
        }
        Arrays.stream(tablesManager.getTranJTable().getSelectedRows())
                .map(tablesManager.getTranJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(tablesManager.getTranTableModel()::deleteRow);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
    }
}
