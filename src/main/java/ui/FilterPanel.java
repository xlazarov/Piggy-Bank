package ui;

import enums.TransactionType;
import model.Category;
import enums.DateSpinnerType;
import enums.TableType;

import javax.swing.*;
import java.util.*;

public class FilterPanel extends JPanel {
    private final JSpinner spinnerFrom, spinnerTo;
    private final JCheckBox checkBoxIncomes, checkBoxSpending;
    private JComboBox<Object> categoriesComboBox;
    private final MessageDialog messageDialog;
    private final TablesManager tablesManager;
    private static final ui.I18N I18N = new I18N(FilterPanel.class);

    public FilterPanel(TablesManager tablesManager, MessageDialog messageDialog){
        this.messageDialog = messageDialog;
        this.tablesManager = tablesManager;
        checkBoxIncomes = new JCheckBox(I18N.getString(TransactionType.INCOME), true);
        checkBoxSpending = new JCheckBox(I18N.getString(TransactionType.SPENDING), true);
        spinnerFrom = new DateSpinner(tablesManager, DateSpinnerType.FROM);
        spinnerTo = new DateSpinner(tablesManager, DateSpinnerType.TO);
        categoriesComboBox = createComboBox();
        setFilterPanel();
        setComponentsEnabled(TableType.STATISTICS.ordinal());
    }

    public JSpinner getSpinnerFrom() {
        return spinnerFrom;
    }

    public JSpinner getSpinnerTo() {
        return spinnerTo;
    }

    public Date getDateFrom() {
        return editSpinnerDate((Date) spinnerFrom.getValue(), -1);
    }

    public Date getDateTo() {
        return editSpinnerDate((Date) spinnerTo.getValue(), 1);
    }

    public JCheckBox getCheckBoxIncomes() {
        return checkBoxIncomes;
    }

    public JCheckBox getCheckBoxSpending() {
        return checkBoxSpending;
    }

    public JComboBox<Object> getCategoriesComboBox() {
        return categoriesComboBox;
    }

    private void setFilterPanel(){
        this.add(new JLabel("|"));
        this.add(checkBoxIncomes);
        this.add(checkBoxSpending);
        this.add(new JLabel("|"));
        this.add(categoriesComboBox);
        this.add(new JLabel("| " + I18N.getString("from")));
        this.add(spinnerFrom);
        this.add(new JLabel(I18N.getString("to")));
        this.add(spinnerTo);
    }

    private Date editSpinnerDate(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public void setComponentsEnabled(int selectedTable){
        if (selectedTable == TableType.TRANSACTIONS.ordinal()){
            checkBoxIncomes.setEnabled(true);
            checkBoxSpending.setEnabled(true);
            spinnerFrom.setEnabled(true);
            spinnerTo.setEnabled(true);
            categoriesComboBox.setEnabled(true);

        } else if (selectedTable == TableType.STATISTICS.ordinal()){
            checkBoxIncomes.setEnabled(false);
            checkBoxSpending.setEnabled(false);
            spinnerFrom.setEnabled(true);
            spinnerTo.setEnabled(true);
            categoriesComboBox.setEnabled(false);

        } else if (selectedTable == TableType.CATEGORIES.ordinal()){
            checkBoxIncomes.setEnabled(false);
            checkBoxSpending.setEnabled(false);
            spinnerFrom.setEnabled(false);
            spinnerTo.setEnabled(false);
            categoriesComboBox.setEnabled(false);
        }
    }

    public void checkSpinnersValues(){
        Date startDate = (Date) spinnerFrom.getValue();
        Date endDate = (Date) spinnerTo.getValue();
        if (startDate.after(endDate)) {
            spinnerFrom.setValue(spinnerTo.getValue());
        }
    }

    private JComboBox<Object> createComboBox() {
        categoriesComboBox = new JComboBox<>(this.tablesManager.getCatTableModel().getCategories().toArray());
        categoriesComboBox.insertItemAt(new Category(I18N.getString("all"), null), 0);
        categoriesComboBox.setSelectedIndex(0);
        return categoriesComboBox;
    }

    public void updateCategoriesComboCox(){
        categoriesComboBox.removeAllItems();
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(fillComboBox());
        categoriesComboBox.setModel(model);
    }


    public String[] fillComboBox(){
        List<Category> categories = this.tablesManager.getCatTableModel().getCategories();
        String[] resultArray = new String[categories.size() + 1];
        resultArray[0] = I18N.getString("all");
        for (int i = 0; i < categories.size(); i++)
        {
            resultArray[i + 1] = categories.get(i).getName();
        }
        return resultArray;

    }
}
