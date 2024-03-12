package ui;

import enums.DateSpinnerType;
import enums.TransactionType;
import model.Category;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;

public class AddTransaction extends AbstractAddEditTransaction {
    private static final I18N I18N = new I18N(AddTransaction.class);

    public AddTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        dialog = createDialog(I18N.getString("addTransaction"), 230, 330);
        nameField = createTextField(I18N.getString("name"), "", 17);
        amountField = createTextField(I18N.getString("amount"), "", 17);
        noteField = createTextField(I18N.getString("note"), "", 17);

        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        for (String c : tablesManager.getCatTableModel().getCategoriesNames()){
            if (c.equals("Others")){
                checkBoxPanel.add(new Checkbox(c, true));
            } else {
                checkBoxPanel.add(new Checkbox(c, false));
            }
        }
    }

    public void add() {
        initializeComponents();
        createTransactionDialog(I18N.getString("add"));
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountField.getText().replace(",", ".")).abs();
        } catch (NumberFormatException ex) {
            messageDialog.showErrorMessage(I18N.getString("errorMessage"));
            return;
        }
        // ToDo
        Category category = tablesManager.getCatTableModel().getCategories().get(0);
        TransactionType type = (TransactionType) transactionType.getItemAt(transactionType.getSelectedIndex());
        Date date = (Date) spinner.getValue();

        // ToDo
        Transaction newTransaction = new Transaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getTranTableModel().addTransaction(newTransaction);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
        tablesManager.getTranTableModel().filterTransactions();
        dialog.dispose();
    }
}
