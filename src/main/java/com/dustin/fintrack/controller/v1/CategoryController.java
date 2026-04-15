package com.dustin.fintrack.controller.v1;

import com.dustin.fintrack.dto.v1.request.CategoryRequestDTO;
import com.dustin.fintrack.dto.v1.response.CategoryResponseDTO;
import com.dustin.fintrack.model.User;
import com.dustin.fintrack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody CategoryRequestDTO category,
            @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(categoryService.create(category, user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.listAll(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.update(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        categoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryService.findById(id, user));
    }
}