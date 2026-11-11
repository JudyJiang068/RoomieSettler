package com.financialcalculator.model;

import java.math.BigDecimal;

public record SplitResponse(
        BigDecimal totalAmount,
        int numberOfPeople,
        BigDecimal eachPersonPays
) {
}