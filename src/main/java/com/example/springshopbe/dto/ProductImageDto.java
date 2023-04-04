package com.example.springshopbe.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    private Long id;
    private String uid;
    private String name;
    private String fileName;
    private String url;
    private String status;
    private String response = "{\"status\": \"success\"}";
}
