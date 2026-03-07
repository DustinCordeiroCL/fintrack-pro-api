package com.dustin.fintrack.dto.v1.request;


import com.dustin.fintrack.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String color;
    private String description;
    private CategoryType categoryType;
    private BigDecimal spendingLimit;
}