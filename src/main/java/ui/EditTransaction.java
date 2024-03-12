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

public class EditTransaction extends AbstractAddEditTransaction {

    private Transaction selectedTransaction;
    private static final I18N I18N = new I18N(EditTransaction.class);

    public EditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void initializeComponents() {
        selectedTransaction = tablesManager.getTranTableModel().getEntity(tablesManager.getTranJTable().getSelectedRow());
        dialog = createDialog(I18N.getString("title"), 230, 330);
        nameField = createTextField(I18N.getString("name"), selectedTransaction.getName(), 17);
        amountField = createTextField(I18N.getString("amount"), String.valueOf(selectedTransaction.getAmount()), 17);
        noteField = createTextField(I18N.getString("note"), selectedTransaction.getNote(), 17);
        transactionType = new JComboBox<>(TransactionType.values());
        spinner = new DateSpinner(tablesManager, DateSpinnerType.TO);
        spinner.setValue(selectedTransaction.getDate());
        transactionType.setSelectedItem(selectedTransaction.getType());
        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        for (String c : tablesManager.getCatTableModel().getCategoriesNames()){
            for (String n : tablesManager.getCatTableModel().getCategoriesNames()){
                if (c.equals(n)){
                    checkBoxPanel.add(new Checkbox(c, true));
                } else {
                    checkBoxPanel.add(new Checkbox(c, false));
                }
            }
        }

    }

    public void edit() {
        initializeComponents();
        createTransactionDialog(I18N.getString("save"));
    }

    private void updateTransaction(String name, BigDecimal  amount, Category category, Date date, String note, TransactionType type) {
        selectedTransaction.setName(name);
        selectedTransaction.setAmount(amount);
        selectedTransaction.setCategory(category);
        selectedTransaction.setDate(date);
        selectedTransaction.setNote(note);
        selectedTransaction.setType(type);
        tablesManager.getTranTableModel().updateEntity(selectedTransaction);
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
        updateTransaction(nameField.getText(), amount, category, date, noteField.getText(), type);
        tablesManager.getStatTableModel().update();
        tablesManager.getStatBalTableModel().update();
        tablesManager.getTranTableModel().filterTransactions();
        dialog.dispose();
    }

}
