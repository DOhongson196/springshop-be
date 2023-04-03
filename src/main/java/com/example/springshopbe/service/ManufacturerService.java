package com.example.springshopbe.service;


import com.example.springshopbe.domain.Manufacturer;
import com.example.springshopbe.dto.ManufacturerDto;
import com.example.springshopbe.exception.ManufacturerException;
import com.example.springshopbe.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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

    public Manufacturer updateManufacturer(Long id,ManufacturerDto dto){
        var found = manufacturerRepository.findById(id);

        if(found.isEmpty()){
            throw new ManufacturerException("Manufacturer is not found");
        }
        var preLogo = found.get().getLogo();
        Manufacturer entity = new Manufacturer();

        BeanUtils.copyProperties(dto,entity);
        if(dto.getLogoFile() != null){
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());
            entity.setLogo(filename);
            dto.setLogoFile(null);
        }
        if(entity.getLogo() == null){
            entity.setLogo(preLogo);
        }
        return manufacturerRepository.save(entity);
    }

    public List<?> findAll(){
        return manufacturerRepository.findAll();
    }

    public Page<Manufacturer> findAll(Pageable pageable){
        return manufacturerRepository.findAll(pageable);
    }

    public Page<Manufacturer> findByName(String name,Pageable pageable){
        return manufacturerRepository.findByNameContainsIgnoreCase(name,pageable);
    }

    public Manufacturer findById(Long id){
        Optional<Manufacturer> found = manufacturerRepository.findById(id);
        if(found.isEmpty()){
            throw new ManufacturerException("Not Found id: " + id);
        }
        return found.get();
    }

    public void deleteById(Long id){
        Manufacturer existed = findById(id);

        manufacturerRepository.delete(existed);
    }
}
