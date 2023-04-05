package com.example.springshopbe.dto;

import com.example.springshopbe.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductBriefDto {
    private Long id;

    private String name;

    private Integer quantity;

    private Double price;

    private Float discount;
    private Long viewCount;
    private Boolean isFeatured;
    private String brief;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;
    private ProductStatus status;

    private String categoryName;
    private String manufacturerName;

    private String imageFilename;


}
