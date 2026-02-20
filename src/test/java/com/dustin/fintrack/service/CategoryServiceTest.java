package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
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
import java.util.Optional;

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

    @Test
    @DisplayName("Should update category when ID exists")
    void updateShouldReturnUpdatedDtoWhenIdExists() {
        // Arrange
        Long id = 1L;
        CategoryDTO dtoToUpdate = new CategoryDTO(null, "Novo Nome", "#FFF", "Nova Descrição");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CategoryDTO result = categoryService.update(id, dtoToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("#FFF", result.getColor());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void updateShouldThrowExceptionWhenIdDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        CategoryDTO dto = new CategoryDTO(null, "Erro", "#000", null);
        when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(nonExistentId, dto);
        });
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should call deleteById when ID exists")
    void deleteShouldDoNothingWhenIdExists() {
        // Arrange
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // Act & Assert
        assertDoesNotThrow(() -> categoryService.delete(id));
        verify(categoryRepository, times(1)).deleteById(id);
    }
}