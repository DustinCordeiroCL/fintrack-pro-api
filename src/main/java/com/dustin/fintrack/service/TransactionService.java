package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.repository.TransactionRepository;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.model.TransactionType;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import  java.time.LocalDateTime;

import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(request.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setCategory(category);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> listAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData(LocalDateTime start, LocalDateTime end) {
        List<Transaction> transactions = transactionRepository.findByDateRange(start, end);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<TransactionResponseDTO> transactionDTOs = transactions.stream()
                .map(TransactionResponseDTO::new)
                .collect(Collectors.toList());

        return new DashboardResponseDTO(totalIncome, totalExpense, balance, transactionDTOs);
    }
}