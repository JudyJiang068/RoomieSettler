package com.financialcalculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/items/{itemId}/shares")
@CrossOrigin(origins = "*")
public class ExpenseShareController {

    private final JdbcTemplate jdbcTemplate;

    public ExpenseShareController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Assign expenses to roommates
    @PostMapping
    @Transactional
    public ResponseEntity<?> assignShares(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> request) {

        // Check whether item exists
        List<Map<String, Object>> items = jdbcTemplate.queryForList(
                "SELECT * FROM bill_items WHERE id = ?",
                itemId
        );

        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Expense item not found."));
        }

        BigDecimal itemAmount =
                (BigDecimal) items.get(0).get("amount");

        Object sharesObject = request.get("shares");

        if (!(sharesObject instanceof List<?> shares)
                || shares.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "At least one share is required."));
        }

        BigDecimal totalShares = BigDecimal.ZERO;
        Set<String> memberNames = new HashSet<>();

        // Validate all shares before saving
        for (Object shareObject : shares) {

            if (!(shareObject instanceof Map<?, ?> share)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid share data."));
            }

            Object nameObject = share.get("memberName");
            Object amountObject = share.get("shareAmount");

            if (nameObject == null || amountObject == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Member name and amount are required."));
            }

            String memberName = nameObject.toString().trim();

            if (memberName.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Member name cannot be empty."));
            }

            if (!memberNames.add(memberName.toLowerCase())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Duplicate member names are not allowed."));
            }

            BigDecimal shareAmount;

            try {
                shareAmount = new BigDecimal(amountObject.toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid share amount."));
            }

            if (shareAmount.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Share amount cannot be negative."));
            }

            totalShares = totalShares.add(shareAmount);
        }

        // Ensure assigned amounts equal the original item amount
        if (totalShares.compareTo(itemAmount) != 0) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "The total shares must equal the item amount.",
                            "itemAmount", itemAmount,
                            "assignedAmount", totalShares
                    )
            );
        }

        // Replace previous assignments
        jdbcTemplate.update(
                "DELETE FROM expense_shares WHERE item_id = ?",
                itemId
        );

        // Save new assignments
        String insertSql = """
                INSERT INTO expense_shares
                (item_id, member_name, share_amount)
                VALUES (?, ?, ?)
                """;

        for (Object shareObject : shares) {

            Map<?, ?> share = (Map<?, ?>) shareObject;

            String memberName =
                    share.get("memberName").toString().trim();

            BigDecimal shareAmount =
                    new BigDecimal(share.get("shareAmount").toString());

            jdbcTemplate.update(
                    insertSql,
                    itemId,
                    memberName,
                    shareAmount
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message", "Expense shares saved successfully.",
                        "itemId", itemId,
                        "totalAssigned", totalShares
                )
        );
    }

    // Get all shares for an expense item
    @GetMapping
    public List<Map<String, Object>> getShares(
            @PathVariable Long itemId) {

        String sql = """
                SELECT id, item_id, member_name, share_amount
                FROM expense_shares
                WHERE item_id = ?
                ORDER BY member_name
                """;

        return jdbcTemplate.queryForList(sql, itemId);
    }
}