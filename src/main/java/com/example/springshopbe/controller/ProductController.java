package com.example.springshopbe.controller;

import com.example.springshopbe.dto.ProductDto;
import com.example.springshopbe.dto.ProductImageDto;
import com.example.springshopbe.exception.FileStorageException;
import com.example.springshopbe.service.FileStorageService;
import com.example.springshopbe.service.MapValidationErrorService;
import com.example.springshopbe.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto dto, BindingResult bindingResult){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(bindingResult);
        if(responseEntity!=null){
            return responseEntity;
        }

        var savedDto = productService.insertProduct(dto);

        return new ResponseEntity<>(savedDto,HttpStatus.CREATED);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadProductImageFileAsResource(filename);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex){
            throw new FileStorageException("File not found");
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
                        +resource.getFilename() + "\"")
                .body(resource);
    }

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
