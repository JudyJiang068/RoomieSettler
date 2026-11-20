package com.financialcalculator.model;

import java.math.BigDecimal;

public class BillItem {

    private Long id;
    private Long billId;
    private String itemName;
    private BigDecimal amount;

    public BillItem() {
    }

    public BillItem(Long id, Long billId,
                    String itemName, BigDecimal amount) {
        this.id = id;
        this.billId = billId;
        this.itemName = itemName;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getBillId() {
        return billId;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}