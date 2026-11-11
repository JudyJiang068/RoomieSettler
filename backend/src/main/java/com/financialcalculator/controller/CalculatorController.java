package com.financialcalculator.controller;

import com.financialcalculator.model.SplitResponse;
import com.financialcalculator.service.CalculatorService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(
            CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

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
}