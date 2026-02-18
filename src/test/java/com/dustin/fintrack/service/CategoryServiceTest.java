package com.dustin.fintrack.service;

import com.dustin.fintrack.dto.v1.CategoryDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        // Initialize a test entity
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setColor("#000000");
    }

    @Test
    @DisplayName("Should save a category and return CategoryDTO")
    void shouldSaveCategory() {
        // Arrange: Define behavior for the mock
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act: Call the method under test
        CategoryDTO result = categoryService.create(category);

        // Assert: Verify results
        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should list all categories as DTOs")
    void shouldListAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act
        List<CategoryDTO> result = categoryService.listAll();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }
}