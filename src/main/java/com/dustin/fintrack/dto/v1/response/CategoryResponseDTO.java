package com.dustin.fintrack.dto.v1.response;

import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private CategoryType categoryType;
    private BigDecimal spendingLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponseDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.description = entity.getDescription();
        this.categoryType = entity.getCategoryType();
        this.spendingLimit = entity.getSpendingLimit();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}