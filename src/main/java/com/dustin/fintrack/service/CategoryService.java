package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.request.CategoryRequestDTO;
import com.dustin.fintrack.dto.v1.response.CategoryResponseDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setColor(request.getColor());
        category.setDescription(request.getDescription());
        category.setCategoryType(request.getCategoryType());
        category.setSpendingLimit(request.getSpendingLimit());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> listAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDTO::new)
                .toList();
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        Optional.ofNullable(dto.getName()).ifPresent(existingCategory::setName);
        Optional.ofNullable(dto.getColor()).ifPresent(existingCategory::setColor);
        Optional.ofNullable(dto.getDescription()).ifPresent(existingCategory::setDescription);
        Optional.ofNullable(dto.getCategoryType()).ifPresent(existingCategory::setCategoryType);
        Optional.ofNullable(dto.getSpendingLimit()).ifPresent(existingCategory::setSpendingLimit);

        return new CategoryResponseDTO(categoryRepository.save(existingCategory));
    }

    public void delete(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.deleteById(existingCategory.getId());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        return new CategoryResponseDTO(category);
    }
}