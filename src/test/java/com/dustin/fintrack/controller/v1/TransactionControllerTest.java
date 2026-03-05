package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
import com.dustin.fintrack.dto.v1.response.DashboardResponseDTO;
import com.dustin.fintrack.dto.v1.response.TransactionResponseDTO;
import com.dustin.fintrack.model.TransactionType;
import com.dustin.fintrack.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 200 OK and list of transactions (Positive)")
    void listAllSuccess() throws Exception {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Salary");
        response.setAmount(new BigDecimal("5000.0"));
        response.setType(TransactionType.INCOME);

        when(transactionService.listAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("Salary"))
                .andExpect(jsonPath("$[0].amount").value(5000.0));
    }

    @Test
    @DisplayName("Should return 201 Created when transaction is valid (Positive)")
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

        when(transactionService.create(any(TransactionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Valid Transaction"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when amount is negative (Negative)")
    void createFailureInvalidData() throws Exception {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setDescription("");
        request.setAmount(new BigDecimal("-10.0"));

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 200 OK and Dashboard date")
    void getDashboardSuccess() throws Exception {
        DashboardResponseDTO dashboardResponse = new DashboardResponseDTO();
        dashboardResponse.setTotalIncome(new BigDecimal("1000.0"));
        dashboardResponse.setTotalExpense(new BigDecimal("400.0"));
        dashboardResponse.setBalance(new BigDecimal("600.0"));
        dashboardResponse.setTransactions(List.of());

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 31, 23, 59);

        when(transactionService.getDashboardData(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(dashboardResponse);

        try {
            mockMvc.perform(get("/api/v1/transactions/dashboard")
                            .param("start", "2026-01-01T00:00:00")
                            .param("end", "2026-01-31T23:59:59")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalIncome").value(1000.0))
                    .andExpect(jsonPath("$.totalExpense").value(400.0))
                    .andExpect(jsonPath("$.balance").value(600.0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should return 400 Bad Request when date parameters are missing")
    void getDashboardMissingParams() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/dashboard")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}