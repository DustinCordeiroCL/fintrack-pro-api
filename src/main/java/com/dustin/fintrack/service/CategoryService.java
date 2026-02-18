package com.dustin.fintrack.service;

import com.dustin.fintrack.dto.v1.CategoryDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}