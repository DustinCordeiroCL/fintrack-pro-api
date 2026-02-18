package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.CategoryDTO;
import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody Category category) {
        // Now returns the decoupled DTO
        CategoryDTO createdCategory = categoryService.create(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listAll() {
        // Standardized list return using DTOs
        List<CategoryDTO> categories = categoryService.listAll();
        return ResponseEntity.ok(categories);
    }
}