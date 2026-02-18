package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.TransactionDTO;
import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing financial transactions.
 * Refactored to handle DTOs as part of Issue #5.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody Transaction transaction) {
        // Business layer now returns a DTO instead of an Entity
        TransactionDTO createdTransaction = transactionService.create(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> listAll() {
        // Business layer now returns a List of DTOs
        List<TransactionDTO> transactions = transactionService.listAll();
        return ResponseEntity.ok(transactions);
    }
}