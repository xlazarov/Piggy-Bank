package ui;

import enums.TableType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

final class DeleteAction extends AbstractAction {
    private final DeleteCategory deleteCategory;
    private final DeleteTransaction deleteTransaction;
    private int selectedTabIndex = 0;
    private static final I18N I18N = new I18N(DeleteAction.class);

    public DeleteAction(TablesManager tablesManager, MessageDialog messageDialog) {
        super(I18N.getString("name"), Icons.DELETE_ICON);
        this.setEnabled(false);
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        this.deleteCategory = new DeleteCategory(tablesManager, messageDialog);
        this.deleteTransaction = new DeleteTransaction(tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal()){
            putValue(SHORT_DESCRIPTION, I18N.getString("descriptionTransaction"));
        } else if (selectedTabIndex == TableType.STATISTICS.ordinal()){
            putValue(SHORT_DESCRIPTION, null);
        } else {
            putValue(SHORT_DESCRIPTION,  I18N.getString("descriptionCategory"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex == 1) {
            deleteTransaction.delete();
        } else if (selectedTabIndex == 2) {
            deleteCategory.delete();
        }
    }
}
