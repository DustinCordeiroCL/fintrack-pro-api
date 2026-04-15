package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.model.TransactionType;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.service.JwtService;
import com.dustin.fintrack.service.TransactionService;
import com.dustin.fintrack.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@test.com");
        mockUser.setName("Test User");
        mockUser.setPassword("password");
    }

    @Test
    @DisplayName("Should return 200 OK and paginated list of transactions")
    void listAllSuccess() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Salary");
        response.setAmount(new BigDecimal("5000.0"));
        response.setType(TransactionType.INCOME);

        Page<TransactionResponseDTO> page = new PageImpl<>(List.of(response));
        when(transactionService.listAllPaged(any(), any(), any(User.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/transactions")
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Salary"))
                .andExpect(jsonPath("$.content[0].amount").value(5000.0))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return 200 OK filtering by type EXPENSE")
    void listAllWithTypeFilter() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(2L);
        response.setDescription("Market");
        response.setType(TransactionType.EXPENSE);

        Page<TransactionResponseDTO> page = new PageImpl<>(List.of(response));
        when(transactionService.listAllPaged(any(), any(), any(User.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/transactions")
                        .with(user(mockUser))
                        .param("type", "EXPENSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Market"));
    }

    @Test
    @DisplayName("Should return 201 Created when transaction is valid")
    void createSuccess() throws Exception {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("Valid Transaction");
        request.setAmount(new BigDecimal("100.0"));
        request.setDate(LocalDateTime.now());
        request.setType(TransactionType.INCOME);
        request.setCategoryId(1L);

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Valid Transaction");

        when(transactionService.create(any(TransactionRequestDTO.class), any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/transactions")
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Valid Transaction"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when amount is negative")
    void createFailureInvalidData() throws Exception {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("");
        request.setAmount(new BigDecimal("-10.0"));

        mockMvc.perform(post("/api/v1/transactions")
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 200 OK and Dashboard data")
    void getDashboardSuccess() throws Exception {
        DashboardResponseDTO dashboardResponse = new DashboardResponseDTO();
        dashboardResponse.setTotalIncome(new BigDecimal("1000.0"));
        dashboardResponse.setTotalExpense(new BigDecimal("400.0"));
        dashboardResponse.setBalance(new BigDecimal("600.0"));
        dashboardResponse.setTransactions(List.of());
        dashboardResponse.setExpensesByCategory(List.of());
        dashboardResponse.setIncomeByCategory(List.of());

        when(transactionService.getDashboardData(any(LocalDateTime.class), any(LocalDateTime.class), any(User.class)))
                .thenReturn(dashboardResponse);

        mockMvc.perform(get("/api/v1/transactions/dashboard")
                        .with(user(mockUser))
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
        mockMvc.perform(get("/api/v1/transactions/dashboard")
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} should return 200 OK when transaction exists")
    void findByIdSuccess() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Shopping");
        response.setDueDay(10);
        response.setIsPaid(false);

        when(transactionService.findById(eq(1L), any(User.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/transactions/{id}", 1L)
                        .with(user(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Shopping"))
                .andExpect(jsonPath("$.dueDay").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} should return 404 when transaction does not exist")
    void findByIdNotFound() throws Exception {
        when(transactionService.findById(eq(99L), any(User.class)))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id 99"));

        mockMvc.perform(get("/api/v1/transactions/{id}", 99L)
                        .with(user(mockUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/transactions/{id} should return 200 OK when transaction is updated")
    void updateSuccess() throws Exception {
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

        when(transactionService.update(eq(1L), any(TransactionRequestDTO.class), any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/transactions/{id}", 1L)
                        .with(csrf())
                        .with(user(mockUser))
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
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("Erro");
        request.setAmount(new BigDecimal("10.0"));
        request.setDate(LocalDateTime.now());
        request.setType(TransactionType.EXPENSE);
        request.setCategoryId(1L);

        when(transactionService.update(eq(99L), any(TransactionRequestDTO.class), any(User.class)))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id: 99"));

        mockMvc.perform(put("/api/v1/transactions/{id}", 99L)
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/transactions/{id} should return 204 No Content")
    void deleteSuccess() throws Exception {
        doNothing().when(transactionService).delete(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/v1/transactions/{id}", 1L)
                        .with(csrf())
                        .with(user(mockUser)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/transactions/{id} should return 404 when transaction does not exist")
    void deleteNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Transaction not found with id: 99"))
                .when(transactionService).delete(eq(99L), any(User.class));

        mockMvc.perform(delete("/api/v1/transactions/{id}", 99L)
                        .with(csrf())
                        .with(user(mockUser)))
                .andExpect(status().isNotFound());
    }
}