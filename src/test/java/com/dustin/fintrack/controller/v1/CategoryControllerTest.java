package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.FintrackProApiApplication;
import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.CategoryDTO;
import com.dustin.fintrack.model.CategoryType;
import com.dustin.fintrack.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

    @Test
    @DisplayName("PUT /api/v1/categories/{id} should return 200 OK when category is updated")
    void updateShouldReturn200() throws Exception {
        Long id = 1L;
        CategoryDTO dto = new CategoryDTO(id, "Lazer", "#000", "Categoria de testes", CategoryType.ESSENTIAL, new BigDecimal("300000"));
        String jsonBody = objectMapper.writeValueAsString(dto);

        when(categoryService.update(eq(id), any(CategoryDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/categories/{id}", id)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lazer"))
                .andExpect(jsonPath("$.color").value("#000"));
    }

    @Test
    @DisplayName("DELETE /api/v1/categories/{id} should return 204 No Content")
    void deleteShouldReturn204() throws Exception {
        Long id = 1L;

        doNothing().when(categoryService).delete(id);

        mockMvc.perform(delete("/api/v1/categories/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} should return 200 OK when category exists")
    void findByIdShouldReturn200() throws Exception {
        Long id = 1L;
        CategoryDTO dto = new CategoryDTO(id, "Electronics", "#000000", null, CategoryType.ESSENTIAL, new BigDecimal("300000"));

        when(categoryService.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.color").value("#000000"));
    }

    @Test
    @DisplayName("GET /api/v1/categories/{id} should return 404 when category does not exist")
    void findByIdShouldReturn404() throws Exception {
        Long nonExistentId = 99L;

        when(categoryService.findById(nonExistentId))
                .thenThrow(new ResourceNotFoundException("Category not found with id: " + nonExistentId));

        mockMvc.perform(get("/api/v1/categories/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}