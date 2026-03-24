package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.model.TransactionType;
import com.dustin.fintrack.service.JwtService;
import com.dustin.fintrack.service.TransactionService;
import com.dustin.fintrack.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Should return 200 OK and list of transactions")
    void listAllSuccess() throws Exception {
        // Arrange
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Salary");
        response.setAmount(new BigDecimal("5000.0"));
        response.setType(TransactionType.INCOME);

        when(transactionService.listAll()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("Salary"))
                .andExpect(jsonPath("$[0].amount").value(5000.0));
    }

    @Test
    @DisplayName("Should return 201 Created when transaction is valid")
    void createSuccess() throws Exception {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("Valid Transaction");
        request.setAmount(new BigDecimal("100.0"));
        request.setDate(LocalDateTime.now());
        request.setType(TransactionType.INCOME);
        request.setCategoryId(1L);

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Valid Transaction");

        when(transactionService.create(any(TransactionRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Valid Transaction"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when amount is negative")
    void createFailureInvalidData() throws Exception {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("");
        request.setAmount(new BigDecimal("-10.0"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 200 OK and Dashboard data")
    void getDashboardSuccess() throws Exception {
        // Arrange
        DashboardResponseDTO dashboardResponse = new DashboardResponseDTO();
        dashboardResponse.setTotalIncome(new BigDecimal("1000.0"));
        dashboardResponse.setTotalExpense(new BigDecimal("400.0"));
        dashboardResponse.setBalance(new BigDecimal("600.0"));
        dashboardResponse.setTransactions(List.of());

        when(transactionService.getDashboardData(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(dashboardResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/dashboard")
                        .param("start", "2026-01-01T00:00:00")
                        .param("end", "2026-01-31T23:59:59")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(1000.0))
                .andExpect(jsonPath("$.totalExpense").value(400.0))
                .andExpect(jsonPath("$.balance").value(600.0));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when date parameters are missing")
    void getDashboardMissingParams() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} should return 200 OK when transaction exists")
    void findByIdSuccess() throws Exception {
        // Arrange
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Shopping");
        response.setDueDay(10);
        response.setIsPaid(false);

        when(transactionService.findById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Shopping"))
                .andExpect(jsonPath("$.dueDay").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} should return 404 when transaction does not exist")
    void findByIdNotFound() throws Exception {
        // Arrange
        when(transactionService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id 99"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/transactions/{id} should return 200 OK when transaction is updated")
    void updateSuccess() throws Exception {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("Cinema Atualizado");
        request.setAmount(new BigDecimal("80.0"));
        request.setDate(LocalDateTime.now());
        request.setType(TransactionType.EXPENSE);
        request.setCategoryId(1L);
        request.setDueDay(15);
        request.setIsPaid(true);

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Cinema Atualizado");
        response.setDueDay(15);
        response.setIsPaid(true);

        when(transactionService.update(eq(1L), any(TransactionRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/transactions/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Cinema Atualizado"))
                .andExpect(jsonPath("$.dueDay").value(15))
                .andExpect(jsonPath("$.isPaid").value(true));
    }

    @Test
    @DisplayName("PUT /api/v1/transactions/{id} should return 404 when transaction does not exist")
    void updateNotFound() throws Exception {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("Erro");
        request.setAmount(new BigDecimal("10.0"));
        request.setDate(LocalDateTime.now());
        request.setType(TransactionType.EXPENSE);
        request.setCategoryId(1L);

        when(transactionService.update(eq(99L), any(TransactionRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/transactions/{id}", 99L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/transactions/{id} should return 204 No Content")
    void deleteSuccess() throws Exception {
        // Arrange
        doNothing().when(transactionService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/transactions/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/transactions/{id} should return 404 when transaction does not exist")
    void deleteNotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Transaction not found with id: 99"))
                .when(transactionService).delete(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/transactions/{id}", 99L)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}