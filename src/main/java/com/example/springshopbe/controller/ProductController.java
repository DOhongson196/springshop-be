package com.example.springshopbe.controller;

import com.example.springshopbe.dto.ProductImageDto;
import com.example.springshopbe.service.FileStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/images/one",
    consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile imageFile){
        var fileInfo = fileStorageService.storeUploadedProductImageFile(imageFile);
        ProductImageDto dto = new ProductImageDto();
        BeanUtils.copyProperties(fileInfo,dto);
        dto.setUrl("http://localhost:8080/api/v1/products/images" + fileInfo.getFileName());
        dto.setStatus("done");
        return new ResponseEntity<>(dto,HttpStatus.CREATED);

    }
}
