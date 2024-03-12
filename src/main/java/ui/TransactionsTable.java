package ui;

import data.TransactionDao;
import enums.TransactionType;
import model.Category;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionsTable extends AbstractEntityTableModel<Transaction> {
    private static final I18N I18N = new I18N(TransactionsTable.class);

    private static final List<Column<?, Transaction>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("name"), String.class, Transaction::getName),
            Column.readOnly(I18N.getString("amount"), BigDecimal.class, Transaction::getAmount),
            Column.readOnly(I18N.getString("type"), TransactionType.class, Transaction::getType),
            Column.readOnly(I18N.getString("category"), Category.class, Transaction::getCategory),
            Column.readOnly(I18N.getString("categoryColor"), Color.class, Transaction::getCategoryColor),
            Column.readOnly(I18N.getString("created"), Date.class, Transaction::getDate),
            Column.readOnly(I18N.getString("note"), String.class, Transaction::getNote)
    );

    private final TransactionDao transactionDao;
    private List<Transaction> transactions;
    private final CategoriesTable categoriesTable;
    private TransactionsFilter filter;

    TransactionsTable(TransactionDao transactionDao, CategoriesTable categoriesTable) {
        super(COLUMNS);
        this.transactionDao = transactionDao;
        this.categoriesTable = categoriesTable;
        loadTransactions();
        update();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    public void deleteRow(int rowIndex) {
        new RowDeleter(rowIndex).execute();
    }

    public void setFilter(TransactionsFilter filter) {
        this.filter = filter;
    }

    public void loadTransactions() {
        this.transactions = transactionDao.findAll();
    }

    public void update() {
        fireTableDataChanged();
    }

    public void filterTransactions() {
        loadTransactions();
        transactions.removeIf(transaction -> !filter.checkTransaction(transaction));
        update();
    }

    public void changeCategoryToDefault(int rowIndex) {
        var category = categoriesTable.getCategories().get(rowIndex);
        if (!category.getName().equals("Others")){
            transactions.stream()
                    .filter(t -> t.getCategory().getName().equals(category.getName()))
                    .forEach(t -> {
                        t.setCategory(categoriesTable.getOthers());
                        transactionDao.update(t);
                    });
            fireTableDataChanged();
        }
    }

    @Override
    protected Transaction getEntity(int rowIndex) {
        return transactions.get(rowIndex);
    }

    @Override
    protected void updateEntity(Transaction transaction) {
        transactionDao.update(transaction);
        fireTableDataChanged();
    }

    public void addTransaction(Transaction transaction) {
        transactionDao.create(transaction);
        transactions.add(transaction);
        fireTableDataChanged();
    }

    private class RowDeleter extends SwingWorker<Boolean, Integer> {
        private final int rowIndex;

        public RowDeleter(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transactions.get(rowIndex));
            loadTransactions();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            update();
        }
    }

    private class RowAdder extends SwingWorker<Boolean, Integer> {
        private final int rowIndex;

        public RowAdder(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        protected Boolean doInBackground() {
            transactionDao.delete(transactions.get(rowIndex));
            loadTransactions();
            return true;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            update();
        }
    }
}
