package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.FintrackProApiApplication;
import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.CategoryRequestDTO;
import com.dustin.fintrack.dto.v1.response.CategoryResponseDTO;
import com.dustin.fintrack.model.CategoryType;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.service.CategoryService;
import com.dustin.fintrack.service.JwtService;
import com.dustin.fintrack.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = FintrackProApiApplication.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

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
    @DisplayName("POST /api/v1/categories should return 201 Created")
    void createShouldReturn201() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Electronics");
        request.setColor("#000000");
        request.setCategoryType(CategoryType.ESSENTIAL);
        request.setSpendingLimit(new BigDecimal("300000"));

        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "#000000", null, CategoryType.ESSENTIAL, new BigDecimal("300000"));

        when(categoryService.create(any(CategoryRequestDTO.class), any(User.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/v1/categories should return 200 OK with list")
    void listAllShouldReturn200() throws Exception {
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "#000000", null, CategoryType.ESSENTIAL, new BigDecimal("300000"));
        when(categoryService.listAll(any(User.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/categories")
                        .with(user(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    @DisplayName("PUT /api/v1/categories/{id} should return 200 OK when category is updated")
    void updateShouldReturn200() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Lazer");
        request.setColor("#000");
        request.setCategoryType(CategoryType.DISCRETIONARY);
        request.setSpendingLimit(new BigDecimal("300000"));

        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Lazer", "#000", null, CategoryType.DISCRETIONARY, new BigDecimal("300000"));
        when(categoryService.update(eq(1L), any(CategoryRequestDTO.class), any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/categories/{id}", 1L)
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lazer"))
                .andExpect(jsonPath("$.color").value("#000"));
    }

    @Test
    @DisplayName("PUT /api/v1/categories/{id} should return 404 when category does not exist")
    void updateShouldReturn404() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Erro");

        when(categoryService.update(eq(99L), any(CategoryRequestDTO.class), any(User.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with id: 99"));

        mockMvc.perform(put("/api/v1/categories/{id}", 99L)
                        .with(csrf())
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/categories/{id} should return 204 No Content")
    void deleteShouldReturn204() throws Exception {
        doNothing().when(categoryService).delete(1L, mockUser);

        mockMvc.perform(delete("/api/v1/categories/{id}", 1L)
                        .with(csrf())
                        .with(user(mockUser)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/categories/{id} should return 404 when category does not exist")
    void deleteShouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Category not found with id: 99"))
                .when(categoryService).delete(eq(99L), any(User.class));

        mockMvc.perform(delete("/api/v1/categories/{id}", 99L)
                        .with(csrf())
                        .with(user(mockUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} should return 200 OK when category exists")
    void findByIdShouldReturn200() throws Exception {
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "#000000", null, CategoryType.ESSENTIAL, new BigDecimal("300000"));
        when(categoryService.findById(eq(1L), any(User.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/categories/{id}", 1L)
                        .with(user(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.color").value("#000000"));
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} should return 404 when category does not exist")
    void findByIdShouldReturn404() throws Exception {
        when(categoryService.findById(eq(99L), any(User.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with id: 99"));

        mockMvc.perform(get("/api/v1/categories/{id}", 99L)
                        .with(user(mockUser)))
                .andExpect(status().isNotFound());
    }
}