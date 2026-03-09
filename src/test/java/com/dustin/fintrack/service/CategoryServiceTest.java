package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.CategoryRequestDTO;
import com.dustin.fintrack.dto.v1.response.CategoryResponseDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.CategoryType;
import com.dustin.fintrack.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private CategoryRequestDTO request;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setColor("#000000");
        category.setDescription("Unity tests");
        category.setCategoryType(CategoryType.ESSENTIAL);
        category.setSpendingLimit(new BigDecimal("300000"));

        request = new CategoryRequestDTO();
        request.setName("Electronics");
        request.setColor("#000000");
        category.setDescription("Unity tests DTO");
        request.setCategoryType(CategoryType.ESSENTIAL);
        request.setSpendingLimit(new BigDecimal("300000"));
    }

    @Test
    @DisplayName("Should save a category and return CategoryResponseDTO")
    void shouldSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO result = categoryService.create(request);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should list all categories as ResponseDTOs")
    void shouldListAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = categoryService.listAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    @DisplayName("Should update category when ID exists")
    void updateShouldReturnUpdatedDtoWhenIdExists() {
        CategoryRequestDTO requestToUpdate = new CategoryRequestDTO();
        requestToUpdate.setName("Novo Nome");
        requestToUpdate.setColor("#FFF");
        requestToUpdate.setDescription("Unity tests");
        requestToUpdate.setCategoryType(CategoryType.DISCRETIONARY);
        requestToUpdate.setSpendingLimit(new BigDecimal("500000"));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        CategoryResponseDTO result = categoryService.update(1L, requestToUpdate);

        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("#FFF", result.getColor());
        assertEquals("Unity tests", result.getDescription());
        assertEquals(CategoryType.DISCRETIONARY, result.getCategoryType());
        assertEquals(new BigDecimal("500000"), result.getSpendingLimit());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent ID")
    void updateShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(99L, request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should delete category when ID exists")
    void deleteShouldSucceedWhenIdExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> categoryService.delete(1L));
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent ID")
    void deleteShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(99L));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return CategoryResponseDTO when ID exists")
    void findByIdShouldReturnDtoWhenIdExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        assertEquals("#000000", result.getColor());
        assertEquals("Unity tests DTO", result.getDescription());
        assertEquals(CategoryType.ESSENTIAL, result.getCategoryType());
        assertEquals(new BigDecimal("300000"), result.getSpendingLimit());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void findByIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(99L));
        verify(categoryRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should persist categoryType and spendingLimit correctly")
    void shouldPersistNewCategoryFields() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO result = categoryService.create(request);

        assertEquals(CategoryType.ESSENTIAL, result.getCategoryType());
        assertEquals(new BigDecimal("300000"), result.getSpendingLimit());
    }
}