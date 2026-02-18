package com.dustin.fintrack.service;

import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.repository.TransactionRepository;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Processes and saves a new transaction after validating the associated category.
     * Part of Issue #3 requirements.
     */
    @Transactional
    public Transaction create(Transaction transaction) {
        // Validation: Ensure the transaction has a category and a valid ID
        if (transaction.getCategory() == null || transaction.getCategory().getId() == null) {
            throw new RuntimeException("Transaction must have a valid category.");
        }

        // Business Rule: Verify if the category exists in the database
        categoryRepository.findById(transaction.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions from the database.
     */
    public List<Transaction> listAll() {
        return transactionRepository.findAll();
    }
}