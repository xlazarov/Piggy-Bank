package ui;

import model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditCategory extends AbstractAddEditCategory {

    private Category selectedCategory;
    private static final I18N I18N = new I18N(EditCategory.class);

    public EditCategory(JFrame frame, TablesManager tablesManager, MessageDialog messageDialog) {
        super(frame, tablesManager, messageDialog);
    }

    private void setCategory(String name, Color color) {
        selectedCategory.setColor(color);
        selectedCategory.setName(name);
    }

    public void edit() {
        selectedCategory = tablesManager.getCatTableModel().getEntity(tablesManager.getCatJTable().getSelectedRow());
        preselectedColor = selectedCategory.getColor();
        prepareColorPanel(preselectedColor);
        dialog = createDialog(I18N.getString("dialogTitle"), 270, 150);
        nameField = createTextField(I18N.getString("name"), selectedCategory.getName(), 17);

        if (selectedCategory.getName().equals("Others")) {
            nameField.setEnabled(false);
            nameField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    messageDialog.showAlertMessage(I18N.getString("message"));
                }
            });
        }

        createCategoryDialog(I18N.getString("save"));
    }

    @Override
    protected void buttonActionPerformed(ActionEvent actionEvent) {
        if (nameField.getText().equals("")){
            messageDialog.showErrorMessage(I18N.getString("errorMessage"));

        } else if (!checkCategoryExistence(new Category(nameField.getText(), categoryColorPanel.getBackground()), selectedCategory)){
            setCategory(nameField.getText(), categoryColorPanel.getBackground());
            tablesManager.getCatTableModel().updateEntity(selectedCategory);
            tablesManager.getStatTableModel().update();
            tablesManager.getTranTableModel().update();
            dialog.dispose();
        }

    }
}
