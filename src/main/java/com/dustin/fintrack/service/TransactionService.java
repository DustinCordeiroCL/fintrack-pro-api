package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.repository.TransactionRepository;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.dustin.fintrack.dto.v1.TransactionDTO;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionDTO create(Transaction transaction) {
        if (transaction.getCategory() == null || transaction.getCategory().getId() == null) {
            throw new RuntimeException("Transaction must have a valid category.");
        }

        categoryRepository.findById(transaction.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException(transaction.getCategory().getId()));

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionDTO(savedTransaction);
    }

    public List<TransactionDTO> listAll() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(TransactionDTO::new)
                .toList();
    }
}