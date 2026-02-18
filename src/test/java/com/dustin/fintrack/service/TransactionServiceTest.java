package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.TransactionDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.Transaction;
import com.dustin.fintrack.model.TransactionType;
import com.dustin.fintrack.repository.CategoryRepository;
import com.dustin.fintrack.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Food");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Dinner");
        transaction.setAmount(new BigDecimal("50.0"));
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(category);
    }

    @Test
    @DisplayName("Should create transaction when category exists")
    void createTransactionSuccess() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        TransactionDTO result = transactionService.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals("Dinner", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category does not exist")
    void createTransactionCategoryNotFound() {
        // Arrange: Category not found in database
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.create(transaction);
        });

        // Verify that save was NEVER called because the exception stopped the flow
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}