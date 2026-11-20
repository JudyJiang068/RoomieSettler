package com.financialcalculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    private final JdbcTemplate jdbcTemplate;

    public BillController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get all bills
    @GetMapping
    public List<Map<String, Object>> getAllBills() {

        String sql = "SELECT * FROM bills ORDER BY id DESC";

        return jdbcTemplate.queryForList(sql);
    }

    // Create a new bill
    @PostMapping
    public ResponseEntity<?> createBill(
            @RequestBody Map<String, String> request) {

        String title = request.get("title");
        String currency = request.get("currency");

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Bill title is required."));
        }

        if (currency == null || currency.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Currency is required."));
        }

        String sql = """
                INSERT INTO bills (title, amount, currency)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                title.trim(),
                BigDecimal.ZERO,
                currency.trim().toUpperCase()
        );

        Long billId = jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Long.class
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Bill created successfully.",
                        "billId", billId,
                        "title", title.trim(),
                        "amount", BigDecimal.ZERO,
                        "currency", currency.trim().toUpperCase()
                )
        );
    }

    // Get one bill by ID
    @GetMapping("/{billId}")
    public ResponseEntity<?> getBillById(
            @PathVariable Long billId) {

        String sql = "SELECT * FROM bills WHERE id = ?";

        List<Map<String, Object>> bills =
                jdbcTemplate.queryForList(sql, billId);

        if (bills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Bill not found."));
        }

        return ResponseEntity.ok(bills.get(0));
    }
}