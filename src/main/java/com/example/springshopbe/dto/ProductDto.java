package com.example.springshopbe.dto;

import com.example.springshopbe.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    @NotEmpty(message = "Name is required")
    private String name;
    @Min(value = 0)
    private Integer quantity;
    @Min(value = 0)
    private Double price;
    @Min(value = 0)
    @Max(value = 100)
    private Float discount;
    private Long viewCount;
    private Boolean isFeatured;
    private String brief;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;
    private ProductStatus status;

    private Long categoryId;
    private Long manufacturerId;

    private List<ProductImageDto> images;

    private ProductImageDto image;
    private CategoryDto category;
    private ManufacturerDto manufacturer;

}
