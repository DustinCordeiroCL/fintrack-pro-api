package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.FintrackProApiApplication;
import com.dustin.fintrack.dto.v1.CategoryDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        CategoryDTO dto = new CategoryDTO(id, "Lazer", "#000", "Categoria de testes");
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
}