package model;

import java.math.BigDecimal;

public class CategoryStatistic {
    private final Category category;
    private int transactionsCounter;
    private BigDecimal expenses;
    private BigDecimal income;
    private BigDecimal percentageInc;
    private BigDecimal percentageSpend;
    private BigDecimal sum;

    public CategoryStatistic(Category category){
        this.category = category;
    }

    public String getCategoryName(){
        return category.getName();
    }

    public Category getCategory() {
        return category;
    }

    public int getTransactionsCounter() {
        return transactionsCounter;
    }

    public void setTransactionsCounter(int transactionsCounter) {
        this.transactionsCounter = transactionsCounter;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getPercentageInc() {
        return percentageInc;
    }

    public void setPercentageInc(BigDecimal percentageInc) {
        this.percentageInc = percentageInc;
    }

    public BigDecimal getPercentageSpend() {
        return percentageSpend;
    }

    public void setPercentageSpend(BigDecimal percentageSpend) {
        this.percentageSpend = percentageSpend;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
