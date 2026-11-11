package com.financialcalculator.service;

import com.financialcalculator.model.SplitResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public SplitResponse calculateSplit(
            BigDecimal amount,
            int people) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }

        if (people <= 0) {
            throw new IllegalArgumentException(
                    "Number of people must be greater than zero."
            );
        }

        BigDecimal result = amount.divide(
                BigDecimal.valueOf(people),
                2,
                RoundingMode.HALF_UP
        );

        return new SplitResponse(
                amount,
                people,
                result
        );
    }
}