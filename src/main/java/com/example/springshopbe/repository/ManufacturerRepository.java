package com.example.springshopbe.repository;

import com.example.springshopbe.domain.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Page<Manufacturer> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<Manufacturer> findByNameContainsIgnoreCase(String name);

    List<Manufacturer> findByIdNotAndNameContainsIgnoreCase(Long id, String name);
}