package com.example.springshopbe.repository;

import com.example.springshopbe.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    List<Manufacturer> findByNameContainsIgnoreCase(String name);

    List<Manufacturer> findByIdNotAndNameContainsIgnoreCase(Long id, String name);
}