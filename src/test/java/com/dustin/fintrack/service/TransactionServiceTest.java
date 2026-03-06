package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
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
import java.time.LocalDateTime;
import java.util.List;
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

    private TransactionRequestDTO requestDTO;
    private Transaction transaction;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Lazer");

        requestDTO = new TransactionRequestDTO();
        requestDTO.setDescription("Cinema");
        requestDTO.setAmount(new BigDecimal("50.0"));
        requestDTO.setDate(LocalDateTime.now());
        requestDTO.setType(TransactionType.EXPENSE);
        requestDTO.setCategoryId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Cinema");
        transaction.setAmount(new BigDecimal("50.0"));
        transaction.setDueDay(10);
        transaction.setIsPaid(false);
        transaction.setCategory(category);
    }

    @Test
    @DisplayName("Should create transaction successfully (Positive)")
    void createSuccess() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = transactionService.create(requestDTO);

        assertNotNull(result);
        assertEquals("Cinema", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category not found (Negative)")
    void createCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.create(requestDTO));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should return a list of TransactionResponseDTO (Positive)")
    void listAllSuccess() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<TransactionResponseDTO> result = transactionService.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cinema", result.get(0).getDescription());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no transactions exist (Positive)")
    void listAllEmpty() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<TransactionResponseDTO> result = transactionService.listAll();

        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should calculate dashboard totals and balance correctly")
    void getDashboardDataSuccess() {
        Transaction income = new Transaction();
        income.setAmount(new BigDecimal("100.0"));
        income.setType(TransactionType.INCOME);
        income.setCategory(category);

        Transaction expense = new Transaction();
        expense.setAmount(new BigDecimal("40.0"));
        expense.setType(TransactionType.EXPENSE);
        expense.setCategory(category);

        LocalDateTime start = LocalDateTime.now().minusDays(30);
        LocalDateTime end = LocalDateTime.now();

        when(transactionRepository.findByDateRange(any(), any())).thenReturn(List.of(income, expense));

        DashboardResponseDTO result = transactionService.getDashboardData(start, end);

        assertEquals(0, new BigDecimal("100.0").compareTo(result.getTotalIncome()));
        assertEquals(0, new BigDecimal("40.0").compareTo(result.getTotalExpense()));
        assertEquals(0, new BigDecimal("60.0").compareTo(result.getBalance()));
        assertEquals(2, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should return zeroed totals when no transactions are found")
    void getDashboardDataEmpty() {
        when(transactionRepository.findByDateRange(any(), any())).thenReturn(List.of());

        DashboardResponseDTO result = transactionService.getDashboardData(LocalDateTime.now(), LocalDateTime.now());

        assertEquals(BigDecimal.ZERO, result.getTotalIncome());
        assertEquals(BigDecimal.ZERO, result.getTotalExpense());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertTrue(result.getTransactions().isEmpty());
    }

    @Test
    @DisplayName("Should persist dueDay and isPaid correctly")
    void shouldPersistNewTransactionFields() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        TransactionResponseDTO result = transactionService.create(requestDTO);

        assertEquals(10, result.getDueDay());
        assertFalse(result.getIsPaid());
    }
}