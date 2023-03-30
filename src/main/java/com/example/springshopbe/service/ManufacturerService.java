package com.example.springshopbe.service;


import com.example.springshopbe.domain.Manufacturer;
import com.example.springshopbe.dto.ManufacturerDto;
import com.example.springshopbe.exception.ManufacturerException;
import com.example.springshopbe.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Manufacturer insertManufacturer(ManufacturerDto dto){
        List<?> foundedList = manufacturerRepository.findByNameContainsIgnoreCase(dto.getName());

        if(foundedList.size()>0){
            throw new ManufacturerException("Manufacturer name is existed");
        }

        Manufacturer entity = new Manufacturer();

        BeanUtils.copyProperties(dto,entity);
        if(dto.getLogoFile() !=null){
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());

            entity.setLogo(filename);
            dto.setLogoFile(null);
        }
        return manufacturerRepository.save(entity);
    }
}
