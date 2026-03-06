package com.dustin.fintrack.dto.v1;

import com.dustin.fintrack.model.Category;
import com.dustin.fintrack.model.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private CategoryType categoryType;
    private BigDecimal spendingLimit;

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.categoryType = entity.getCategoryType();
        this.spendingLimit = entity.getSpendingLimit();
    }
}