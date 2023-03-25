package com.example.springshopbe.dto;

import com.example.springshopbe.domain.CategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "Category name is required")
    private String name;
    private CategoryStatus status;
}
