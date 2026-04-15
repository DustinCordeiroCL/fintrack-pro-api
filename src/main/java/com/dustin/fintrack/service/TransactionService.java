package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.repository.TransactionRepository;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.model.TransactionType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponseDTO create(TransactionRequestDTO request, User user) {
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setDueDay(request.getDueDay());
        transaction.setIsPaid(request.getIsPaid() != null ? request.getIsPaid() : false);

        return new TransactionResponseDTO(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> listAll(User user) {
        return transactionRepository.findAllByUser(user)
                .stream()
                .map(TransactionResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData(LocalDateTime start, LocalDateTime end, User user) {
        List<Transaction> transactions = transactionRepository.findByUserAndDateRange(user, start, end);

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

    @Transactional(readOnly = true)
    public TransactionResponseDTO findById(Long id, User user) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        return new TransactionResponseDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO update(Long id, TransactionRequestDTO request, User user) {
        Transaction existingTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        Optional.ofNullable(request.getDescription()).ifPresent(existingTransaction::setDescription);
        Optional.ofNullable(request.getAmount()).ifPresent(existingTransaction::setAmount);
        Optional.ofNullable(request.getDate()).ifPresent(existingTransaction::setDate);
        Optional.ofNullable(request.getType()).ifPresent(existingTransaction::setType);
        Optional.ofNullable(request.getDueDay()).ifPresent(existingTransaction::setDueDay);
        Optional.ofNullable(request.getIsPaid()).ifPresent(existingTransaction::setIsPaid);
        Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId -> {
            Category existingCategory = categoryRepository.findByIdAndUser(categoryId, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            existingTransaction.setCategory(existingCategory);
        });

        return new TransactionResponseDTO(transactionRepository.save(existingTransaction));
    }

    @Transactional
    public void delete(Long id, User user) {
        Transaction existingTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        transactionRepository.deleteById(existingTransaction.getId());
    }
}