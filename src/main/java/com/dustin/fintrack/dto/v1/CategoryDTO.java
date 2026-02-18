package com.dustin.fintrack.dto.v1;

import com.dustin.fintrack.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;

    // Constructor to easily map from Entity to DTO
    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
    }
}