package model;

import enums.TransactionType;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

public class Transaction {

    private Long id;
    private BigDecimal amount;
    private String name;
    private Date date;
    private String note;
    private Category category;
    private TransactionType type;

    public Transaction(String name, BigDecimal amount, Category category, Date date, String note, TransactionType type) {
        setType(type);
        setName(name);
        setAmount(amount);
        setCategory(category);
        setDate(date);
        setNote(note);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote(){
        return note;
    }

    public Color getCategoryColor(){
        return category.getColor();
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
