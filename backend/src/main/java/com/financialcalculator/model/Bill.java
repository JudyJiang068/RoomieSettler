package com.financialcalculator.model;

import java.math.BigDecimal;

public class Bill {

    private Long id;
    private String title;
    private BigDecimal amount;
    private String currency;
    private String createdAt;

    public Bill() {
    }

    public Bill(Long id, String title,
                BigDecimal amount, String currency,
                String createdAt) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}