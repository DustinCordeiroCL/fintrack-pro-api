package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.TransactionRequestDTO;
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
    private MockMvc mockMvc; // Simula requisições HTTP sem subir o servidor real

    @MockitoBean // Injeta um Mock do Service no contexto do Spring
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper; // Converte objetos para JSON string

    @Test
    @DisplayName("Should return 200 OK and list of transactions (Positive)")
    void listAllSuccess() throws Exception {
        // 1. ARRANGE: Preparar um DTO de resposta para simular o retorno do Service
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(1L);
        response.setDescription("Salary");
        response.setAmount(new BigDecimal("5000.0"));
        response.setType(TransactionType.INCOME);

        // Simula o comportamento do Service devolvendo uma lista com 1 elemento
        when(transactionService.listAll()).thenReturn(List.of(response));

        // 2. ACT & ASSERT: Simula o GET no endpoint
        // Por que: Validamos se a rota está correta e se o JSON retornado é um Array []
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.size()").value(1)) // Verifica se a lista tem 1 item
                .andExpect(jsonPath("$[0].description").value("Salary")) // Valida o conteúdo do primeiro item
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

        // Simulando o retorno do Service
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
        request.setDescription(""); // Inválido (NotBlank)
        request.setAmount(new BigDecimal("-10.0")); // Inválido (Positive)

        // Comentário Didático: O teste nem chega no Service, o @Valid do Controller barra antes
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}