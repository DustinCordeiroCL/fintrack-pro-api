package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
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
        // Mock da Categoria
        category = new Category();
        category.setId(1L);
        category.setName("Lazer");

        // Mock do DTO de Entrada (Request)
        requestDTO = new TransactionRequestDTO();
        requestDTO.setDescription("Cinema");
        requestDTO.setAmount(new BigDecimal("50.0"));
        requestDTO.setDate(LocalDateTime.now());
        requestDTO.setType(TransactionType.EXPENSE);
        requestDTO.setCategoryId(1L);

        // Mock da Entidade para retorno do Repository
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Cinema");
        transaction.setAmount(new BigDecimal("50.0"));
        transaction.setCategory(category);
    }

    // --- TESTES DO MÉTODO CREATE ---

    @Test
    @DisplayName("Should create transaction successfully (Positive)")
    void createSuccess() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        TransactionResponseDTO result = transactionService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Cinema", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when category not found (Negative)")
    void createCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transactionService.create(requestDTO));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    // --- TESTES DO MÉTODO LISTALL ---

    @Test
    @DisplayName("Should return a list of TransactionResponseDTO (Positive)")
    void listAllSuccess() {
        // Arrange
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        // Act
        List<TransactionResponseDTO> result = transactionService.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cinema", result.get(0).getDescription());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no transactions exist (Positive)")
    void listAllEmpty() {
        // Arrange
        when(transactionRepository.findAll()).thenReturn(List.of());

        // Act
        List<TransactionResponseDTO> result = transactionService.listAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findAll();
    }
}