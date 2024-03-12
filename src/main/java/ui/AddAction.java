package ui;

import enums.TableType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class AddAction extends AbstractAction {

    private final AddTransaction addTransaction;
    private final AddCategory addCategory;
    private int selectedTabIndex = 0;
    private static final I18N I18N = new I18N(AddAction.class);

    public AddAction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(I18N.getString("name"), Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("addTransaction"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.addTransaction = new AddTransaction(frame, tablesManager, messageDialog);
        this.addCategory = new AddCategory(frame, tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal() || selectedTabIndex == TableType.STATISTICS.ordinal()){
            putValue(SHORT_DESCRIPTION, I18N.getString("addTransaction"));
        } else {
            putValue(SHORT_DESCRIPTION, I18N.getString("addCategory"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (selectedTabIndex == TableType.TRANSACTIONS.ordinal() || selectedTabIndex == TableType.STATISTICS.ordinal()) {

            this.addTransaction.add();

        } else if (selectedTabIndex == TableType.CATEGORIES.ordinal()) {

            this.addCategory.add();

        }
    }
}
