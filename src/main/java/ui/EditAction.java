package ui;

import enums.TableType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class EditAction extends AbstractAction {

    private final EditCategory editCategory;
    private final EditTransaction editTransaction;
    private int selectedTabIndex = 0;
    private static final I18N I18N = new I18N(EditAction.class);

    public EditAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(I18N.getString("name"), Icons.EDIT_ICON);
        this.setEnabled(false);
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
        this.editCategory = new EditCategory(frame, tablesManager, messageDialog);
        this.editTransaction = new EditTransaction(frame, tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal()){
            putValue(SHORT_DESCRIPTION, I18N.getString("editsTransaction"));
        } else if (selectedTabIndex == TableType.STATISTICS.ordinal()){
            putValue(SHORT_DESCRIPTION, null);
        } else {
            putValue(SHORT_DESCRIPTION, I18N.getString("editsCategory"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal() || selectedTabIndex == TableType.STATISTICS.ordinal()) {
            this.editTransaction.edit();
        } else if (selectedTabIndex == TableType.CATEGORIES.ordinal()) {
            this.editCategory.edit();
        }
    }

}
