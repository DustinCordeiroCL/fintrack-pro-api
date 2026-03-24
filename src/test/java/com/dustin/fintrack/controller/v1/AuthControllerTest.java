package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.AuthRequestDTO;
import com.dustin.fintrack.dto.v1.request.RegisterRequestDTO;
import com.dustin.fintrack.dto.v1.response.AuthResponseDTO;
import com.dustin.fintrack.dto.v1.response.UserResponseDTO;
import com.dustin.fintrack.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AuthService authService;

    private AuthResponseDTO buildMockResponse() {
        UserResponseDTO user = UserResponseDTO.builder()
                .id(1L).name("Dustin").email("dustin@fintrack.com")
                .createdAt(LocalDateTime.now()).build();
        return AuthResponseDTO.builder()
                .accessToken("jwt-token").refreshToken("refresh-token").user(user).build();
    }

    @Test
    @DisplayName("POST /register - should return 201 and auth response when data is valid")
    void shouldRegisterSuccessfully() throws Exception {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Dustin", "dustin@fintrack.com", "123456");
        when(authService.register(any())).thenReturn(buildMockResponse());

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /register - should return 400 when email is invalid")
    void shouldReturn400WhenEmailInvalid() throws Exception {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Dustin", "not-an-email", "123456");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /login - should return 200 and auth response when credentials are valid")
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO("dustin@fintrack.com", "123456");
        when(authService.login(any())).thenReturn(buildMockResponse());

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /login - should return 400 when fields are blank")
    void shouldReturn400WhenLoginFieldsBlank() throws Exception {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO("", "");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /refresh - should return 200 and new token pair when refresh token is valid")
    void shouldRefreshSuccessfully() throws Exception {
        // Arrange
        when(authService.refreshToken(any())).thenReturn(buildMockResponse());

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"refresh-token\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"));
    }
}