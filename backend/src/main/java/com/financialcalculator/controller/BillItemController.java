package com.financialcalculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills/{billId}/items")
@CrossOrigin(origins = "*")
public class BillItemController {

    private final JdbcTemplate jdbcTemplate;

    public BillItemController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add a receipt item to a bill
    @PostMapping
    public ResponseEntity<?> addItem(
            @PathVariable Long billId,
            @RequestBody Map<String, Object> request) {

        String itemName = (String) request.get("itemName");
        BigDecimal amount;

        try {
            amount = new BigDecimal(
                    request.get("amount").toString()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid item amount."));
        }

        if (itemName == null || itemName.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Item name is required."));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be greater than zero."));
        }

        // Check whether bill exists
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bills WHERE id = ?",
                Integer.class,
                billId
        );

        if (count == null || count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Bill not found."));
        }

        // Insert item
        String insertSql = """
                INSERT INTO bill_items (bill_id, item_name, amount)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                insertSql,
                billId,
                itemName.trim(),
                amount
        );

        // Get inserted item ID
        Long itemId = jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Long.class
        );

        // Update bill total amount
        String updateSql = """
                UPDATE bills
                SET amount = (
                    SELECT COALESCE(SUM(amount), 0)
                    FROM bill_items
                    WHERE bill_id = ?
                )
                WHERE id = ?
                """;

        jdbcTemplate.update(updateSql, billId, billId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Item added successfully.",
                        "itemId", itemId,
                        "billId", billId,
                        "itemName", itemName.trim(),
                        "amount", amount
                )
        );
    }

    // Get all items belonging to a bill
    @GetMapping
    public List<Map<String, Object>> getItems(
            @PathVariable Long billId) {

        String sql = """
                SELECT id, bill_id, item_name, amount
                FROM bill_items
                WHERE bill_id = ?
                ORDER BY id
                """;

        return jdbcTemplate.queryForList(sql, billId);
    }
}