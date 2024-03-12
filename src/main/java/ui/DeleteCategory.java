package ui;

import java.util.Arrays;
import java.util.Comparator;

public class DeleteCategory {
    private final TablesManager tablesManager;
    private final MessageDialog messageDialog;
    private static final I18N I18N = new I18N(DeleteCategory.class);

    public DeleteCategory(TablesManager tablesManager, MessageDialog messageDialog){
        this.tablesManager = tablesManager;
        this.messageDialog = messageDialog;
    }

    public void delete() {
        String message = I18N.getString("message");
        message += messageDialog.createItemsString(tablesManager.getCatJTable());
        if (!messageDialog.showConfirmMessage(message, I18N.getString("title"))){
            return;
        }
        Arrays.stream(tablesManager.getCatJTable().getSelectedRows())
                .map(tablesManager.getCatJTable()::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(e -> {
                    tablesManager.getTranTableModel().changeCategoryToDefault(e);
                    if (!tablesManager.getCatTableModel().deleteRow(e)){
                        messageDialog.showErrorMessage(I18N.getString("errorMessage"));
                    }
                });
        tablesManager.getStatTableModel().update();
    }
}
