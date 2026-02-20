package com.dustin.fintrack.dto.v1;

import com.dustin.fintrack.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;
    private String description;

    // Constructor to easily map from Entity to DTO
    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
    }
}