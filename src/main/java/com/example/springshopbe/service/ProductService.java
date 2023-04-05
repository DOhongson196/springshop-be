package com.example.springshopbe.service;

import com.example.springshopbe.domain.Category;
import com.example.springshopbe.domain.Manufacturer;
import com.example.springshopbe.domain.Product;
import com.example.springshopbe.domain.ProductImage;
import com.example.springshopbe.dto.ProductDto;
import com.example.springshopbe.repository.ProductImageRepository;
import com.example.springshopbe.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Transactional(rollbackFor = Exception.class)
    public ProductDto insertProduct(ProductDto dto){
        Product entity = new Product();
        BeanUtils.copyProperties(dto,entity);

        var manuf = new Manufacturer();
        manuf.setId(dto.getManufacturerId());
        entity.setManufacturer(manuf);

        var cate = new Category();
        cate.setId(dto.getCategoryId());
        entity.setCategory(cate);

        if(dto.getImage() != null){
            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(dto.getImage(),img);
            var savedImg = productImageRepository.save(img);
            entity.setImage(savedImg);
        }

        if(dto.getImages() !=null && dto.getImages().size()>0){
            var entityList = saveProductImages(dto);
            entity.setImages(entityList);
        }

        var savedProduct = productRepository.save(entity);
        dto.setId(savedProduct.getId());

        return dto;
    }

    private Set<ProductImage> saveProductImages(ProductDto dto) {
        var entityList =  new HashSet<ProductImage>();

        var newList = dto.getImages().stream().map(item ->{
            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(item,img);

            var savedImg = productImageRepository.save(img);
            item.setId(savedImg.getId());

            entityList.add(savedImg);
            return item;
        }).collect(Collectors.toList());

        dto.setImages(newList);
        return entityList;
    }
}
