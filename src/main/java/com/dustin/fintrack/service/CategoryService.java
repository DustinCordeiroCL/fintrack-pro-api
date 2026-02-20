package com.dustin.fintrack.service;

import com.dustin.fintrack.controller.exception.ResourceNotFoundException;
import com.dustin.fintrack.dto.v1.CategoryDTO;
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
    public CategoryDTO create(Category category) {
        // Saving the entity and converting the result to DTO
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory);
    }

    public List<CategoryDTO> listAll() {
        List<Category> categories = categoryRepository.findAll();

        // Using Java Streams to map the list of entities to DTOs
        return categories.stream()
                .map(CategoryDTO::new)
                .toList();
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        Optional.ofNullable(dto.getName()).ifPresent(existingCategory::setName);
        Optional.ofNullable(dto.getColor()).ifPresent(existingCategory::setColor);

        return new CategoryDTO(categoryRepository.save(existingCategory));
    }

    public void delete(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.deleteById(existingCategory.getId());
    }
}