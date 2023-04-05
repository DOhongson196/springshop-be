package com.example.springshopbe.service;

import com.example.springshopbe.domain.Category;
import com.example.springshopbe.domain.Manufacturer;
import com.example.springshopbe.domain.Product;
import com.example.springshopbe.domain.ProductImage;
import com.example.springshopbe.dto.ProductBriefDto;
import com.example.springshopbe.dto.ProductDto;
import com.example.springshopbe.dto.ProductImageDto;
import com.example.springshopbe.exception.ProductException;
import com.example.springshopbe.repository.ProductImageRepository;
import com.example.springshopbe.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public Page<ProductBriefDto> getProductBriefsByName(String name, Pageable pageable){
        var list = productRepository.findByNameContainsIgnoreCase(name,pageable);

        var newList = list.getContent().stream().map(item ->{
            ProductBriefDto dto = new ProductBriefDto();
            BeanUtils.copyProperties(item,dto);

            dto.setCategoryName(item.getCategory().getName());
            dto.setManufacturerName(item.getManufacturer().getName());
            dto.setImageFilename(item.getImage().getFilename());

            return dto;
        }).collect(Collectors.toList());

        var newPage = new PageImpl<ProductBriefDto>(newList,list.getPageable(),list.getTotalElements());

        return newPage;
    }
    @Transactional(rollbackFor = Exception.class)
    public ProductDto insertProduct(ProductDto dto){
        Product entity = new Product();
        BeanUtils.copyProperties(dto,entity);

        var manuF = new Manufacturer();
        manuF.setId(dto.getManufacturerId());
        entity.setManufacturer(manuF);

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

    @Transactional(rollbackFor = Exception.class)
    public ProductDto updateProduct(Long id, ProductDto dto){
        var found = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found"));
        String ignoreFields[] = new String[]{"createdDate","image","images","viewCount"};
        BeanUtils.copyProperties(dto,found,ignoreFields);

        if(dto.getImage().getId() != null && found.getImage().getId() != dto.getImage().getId()){
            fileStorageService.deleteProductImageFile(found.getImage().getFilename());
            ProductImage img = new ProductImage();
            BeanUtils.copyProperties(dto.getImages(),img);
            productImageRepository.save(img);
            found.setImage(img);
        }
        var manuF = new Manufacturer();
        manuF.setId(dto.getManufacturerId());
        found.setManufacturer(manuF);

        var cate = new Category();
        cate.setId(dto.getCategoryId());
        found.setCategory(cate);

        if(dto.getImages().size() > 0){
            var toDeleteFile = new ArrayList<ProductImage>();

            found.getImages().stream().forEach(item -> {
                var existed = dto.getImages().stream().anyMatch(img -> img.getId()== item.getId());

                if(!existed) toDeleteFile.add(item);
            });

            if(toDeleteFile.size() >0){
                toDeleteFile.stream().forEach(item ->{
                    fileStorageService.deleteProductImageFile(item.getFilename());
                    productImageRepository.delete(item);
                });
            }
            var imgList = dto.getImages().stream().map(item -> {
                ProductImage img = new ProductImage();
                BeanUtils.copyProperties(item,img);
                return img;
            }).collect(Collectors.toSet());
            found.setImages(imgList);
        }
        var savedEntity = productRepository.save(found);
        dto.setId(savedEntity.getId());
        return dto;
    }

    public ProductDto getEditedProductById(Long id){
        var found = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product Not Found"));
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(found,dto);

        dto.setCategoryId(found.getCategory().getId());
        dto.setManufacturerId(found.getManufacturer().getId());

        var images = found.getImages().stream().map(item ->{
            ProductImageDto imgDto = new ProductImageDto();

            BeanUtils.copyProperties(item,imgDto);
            return imgDto;
        }).collect(Collectors.toList());

        dto.setImages(images);

        ProductImageDto imageDto = new ProductImageDto();
        BeanUtils.copyProperties(found.getImage(),imageDto);
        dto.setImage(imageDto);

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

    @Transactional(rollbackFor = Exception.class)
    public void deleteProductById(Long id){
        var found = productRepository.findById(id)
                .orElseThrow(() ->new ProductException("Product not found"));
        if(found.getImage() != null){
            fileStorageService.deleteProductImageFile(found.getImage().getFilename());

            productImageRepository.delete(found.getImage());
        }
        if(found.getImages().size()>0){
            found.getImages().stream().forEach(item ->{
                fileStorageService.deleteProductImageFile(item.getFilename());
                productImageRepository.delete(item);
            });
        }
        productRepository.delete(found);

    }
}
