package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.CategoryRequestDTO;
import com.dustin.fintrack.dto.v1.response.CategoryResponseDTO;
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
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO category) {
        CategoryResponseDTO createdCategory = categoryService.create(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listAll() {
        List<CategoryResponseDTO> categories = categoryService.listAll();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO updatedCategory = categoryService.update(id, dto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.findById(id);

        return ResponseEntity.ok(category);
    }
}