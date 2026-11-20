package com.financialcalculator.controller;

import com.financialcalculator.model.SplitResponse;
import com.financialcalculator.service.CalculatorService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(
            CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    // Original equal split API
    @GetMapping("/split")
    public ResponseEntity<?> calculateSplit(
            @RequestParam BigDecimal amount,
            @RequestParam int people) {

        try {
            SplitResponse result =
                    calculatorService.calculateSplit(
                            amount, people
                    );

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // Calculate individual totals for a bill
    @GetMapping("/totals")
    public ResponseEntity<?> getIndividualTotals(
            @RequestParam Long billId) {

        if (billId == null || billId <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid bill ID.")
            );
        }

        List<Map<String, Object>> totals =
                calculatorService.getIndividualTotals(billId);

        return ResponseEntity.ok(totals);
    }
}