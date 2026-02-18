package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing financial transactions.
 * Final step of Issue #3.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        // Calls the service layer to validate and persist the transaction
        Transaction createdTransaction = transactionService.create(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> listAll() {
        // Returns the complete list of transactions, including categories
        List<Transaction> transactions = transactionService.listAll();
        return ResponseEntity.ok(transactions);
    }
}