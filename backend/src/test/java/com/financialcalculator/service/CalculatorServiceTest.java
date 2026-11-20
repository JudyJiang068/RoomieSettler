package com.financialcalculator.service;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorServiceTest {
    private final CalculatorService calculatorService =
            new CalculatorService(new JdbcTemplate());

    @Test
    void splitsAmountEquallyBetweenTwoPeople() {
        var split = calculatorService.calculateSplit(new BigDecimal("20.00"), 2);

        assertEquals(new BigDecimal("10.00"), split.eachPersonPays());
    }
}
