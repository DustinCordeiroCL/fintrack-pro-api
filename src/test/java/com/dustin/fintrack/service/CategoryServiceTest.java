package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.CategoryDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.CategoryType;
import com.dustin.fintrack.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setColor("#000000");
        category.setCategoryType(CategoryType.ESSENTIAL);
        category.setSpendingLimit(new BigDecimal("300000"));
    }

    @Test
    @DisplayName("Should save a category and return CategoryDTO")
    void shouldSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should list all categories as DTOs")
    void shouldListAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDTO> result = categoryService.listAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    @DisplayName("Should update category when ID exists")
    void updateShouldReturnUpdatedDtoWhenIdExists() {
        Long id = 1L;
        CategoryDTO dtoToUpdate = new CategoryDTO(null, "Novo Nome", "#FFF", "Nova Descrição", CategoryType.ESSENTIAL, new BigDecimal("300000"));

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        CategoryDTO result = categoryService.update(id, dtoToUpdate);

        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("#FFF", result.getColor());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void updateShouldThrowExceptionWhenIdDoesNotExist() {
        Long nonExistentId = 99L;
        CategoryDTO dto = new CategoryDTO(null, "Erro", "#000", null, CategoryType.ESSENTIAL, new BigDecimal("300000"));
        when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(nonExistentId, dto);
        });
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should call deleteById when ID exists")
    void deleteShouldDoNothingWhenIdExists() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> categoryService.delete(id));
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return CategoryDTO when ID exists")
    void findByIdShouldReturnDtoWhenIdExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        assertEquals("#000000", result.getColor());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void findByIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findById(99L);
        });

        verify(categoryRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should persist categoryType and spendingLimit correctly")
    void shouldPersistNewCategoryFields() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.create(category);

        assertEquals(CategoryType.ESSENTIAL, result.getCategoryType());
        assertEquals(new BigDecimal("300000"), result.getSpendingLimit());
    }
}