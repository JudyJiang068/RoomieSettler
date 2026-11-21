package com.financialcalculator.service;

import com.financialcalculator.model.SplitResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class CalculatorService {

    private final JdbcTemplate jdbcTemplate;

    public CalculatorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Original equal split calculation
    public SplitResponse calculateSplit(
            BigDecimal amount,
            int people) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
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

    // Calculate each member's total expense for one bill
    public List<Map<String, Object>> getIndividualTotals(
            Long billId) {

        String sql = """
                SELECT
                    es.member_name AS memberName,
                    SUM(es.share_amount) AS totalAmount
                FROM expense_shares es
                JOIN bill_items bi
                    ON es.item_id = bi.id
                WHERE bi.bill_id = ?
                GROUP BY es.member_name
                ORDER BY es.member_name
                """;

        return jdbcTemplate.queryForList(sql, billId);
    }
}