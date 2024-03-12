package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class AbstractAddEditTransaction extends AbstractAddEditAction {

    protected JTextField nameField, amountField, noteField;
    protected JComboBox<Object> transactionType;
    protected JDialog categoriesDialog;
    protected JSpinner spinner;
    private static final I18N I18N = new I18N(AbstractAddEditTransaction.class);

    protected JButton selectCatButton = new JButton(I18N.getString("select"));
    protected JButton confirmButton = new JButton(I18N.getString("save"));

    protected JPanel checkBoxPanel;

    protected AbstractAddEditTransaction(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog){
        super(frame, tablesManager, messageDialog);
        selectCatButton.addActionListener(this::createCheckBoxDialog);
        confirmButton.addActionListener(this::disposeDialog);
    }

    protected void disposeDialog(ActionEvent e){
        categoriesDialog.dispose();
    }

    protected void createCheckBoxDialog(ActionEvent e){
        categoriesDialog = new JDialog();
        categoriesDialog.setTitle("Select categories");
        categoriesDialog.setSize(new Dimension(170, 180));
        categoriesDialog.setLayout(new FlowLayout());
        categoriesDialog.setModal(true);
        categoriesDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        categoriesDialog.setLocationRelativeTo(dialog);
        JScrollPane pane = new JScrollPane(checkBoxPanel);
        pane.setPreferredSize(new Dimension(150, 100));
        categoriesDialog.add(pane);
        categoriesDialog.add(confirmButton);
        dialog.getRootPane().setDefaultButton(confirmButton);
        categoriesDialog.getContentPane().add(confirmButton);
        categoriesDialog.setResizable(false);
        categoriesDialog.setVisible(true);
    }

    protected void createTransactionDialog(String buttonTitle){
        dialog.add(new JLabel(I18N.getString("categoryLabel")));
        dialog.add(selectCatButton);
        dialog.add(new JLabel(I18N.getString("typeLabel")));
        dialog.add(transactionType);
        dialog.add(new JLabel(I18N.getString("dateLabel")));
        dialog.add(spinner);
        JButton button = createButton(buttonTitle);
        dialog.getContentPane().add(button);
        dialog.getRootPane().setDefaultButton(button);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

}
