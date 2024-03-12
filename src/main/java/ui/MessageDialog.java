package ui;

import javax.swing.*;

public class MessageDialog {
    private final JFrame parentFrame;
    private static final I18N I18N = new I18N(MessageDialog.class);

    public MessageDialog(JFrame parentFrame){
        this.parentFrame = parentFrame;
    }

    public void showAlertMessage(String message){
        JOptionPane.showMessageDialog(parentFrame, message, I18N.getString("alert"), JOptionPane.WARNING_MESSAGE);
    }

    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(parentFrame, message, I18N.getString("error"), JOptionPane.ERROR_MESSAGE);
    }

    public boolean showConfirmMessage(String message, String title){
        return JOptionPane.showConfirmDialog(parentFrame, message, title, JOptionPane.OK_CANCEL_OPTION) == 0;
    }

    public String createItemsString(JTable table){
        String message = "";
        int count = 1;
        for (int i : table.getSelectedRows()){
            message += "\n        " + count + ". " + table.getValueAt(i, 0).toString();
            ++count;
        }
        return message;
    }
}
