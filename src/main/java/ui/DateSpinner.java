package ui;

import enums.DateSpinnerType;
import model.Transaction;
import ui.TablesManager;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class DateSpinner extends JSpinner {

    private final TablesManager tablesManager;
    private final DateSpinnerType type;

    public DateSpinner(TablesManager tablesManager, DateSpinnerType type){
        this.tablesManager = tablesManager;
        this.type = type;
        setDateSpinner();
    }

    private void setDateSpinner() {
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        this.setModel(spinnerDateModel);
        this.setEditor(new JSpinner.DateEditor(this, "dd/MM/yyyy"));
        if (type == DateSpinnerType.FROM) {
            setFromSpinner(spinnerDateModel);
        } else {
            setToSpinner(spinnerDateModel);
        }
        this.setVisible(true);
    }

    private void setFromSpinner(SpinnerDateModel spinnerDateModel) {
        List<Transaction> tranList = tablesManager.getTranTableModel().getTransactions();
        if (!tranList.isEmpty()) {
            Date minDate = tranList.stream().map(Transaction::getDate).min(Date::compareTo).get();
            spinnerDateModel.setValue(minDate);
        }
    }

    private void setToSpinner(SpinnerDateModel spinnerDateModel) {
        List<Transaction> tranList = tablesManager.getTranTableModel().getTransactions();
        if (!tranList.isEmpty()) {
            Date minDate = tranList.stream().map(Transaction::getDate).max(Date::compareTo).get();
            spinnerDateModel.setValue(minDate);
        }
    }
}
