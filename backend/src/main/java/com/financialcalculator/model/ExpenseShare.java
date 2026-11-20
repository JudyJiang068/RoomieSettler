package com.financialcalculator.model;

import java.math.BigDecimal;

public class ExpenseShare {

    private Long id;
    private Long itemId;
    private String memberName;
    private BigDecimal shareAmount;

    public ExpenseShare() {
    }

    public ExpenseShare(Long id, Long itemId,
                        String memberName, BigDecimal shareAmount) {
        this.id = id;
        this.itemId = itemId;
        this.memberName = memberName;
        this.shareAmount = shareAmount;
    }

    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getMemberName() {
        return memberName;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }
}