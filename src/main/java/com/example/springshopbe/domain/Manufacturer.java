package com.example.springshopbe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manufacturer")
public class Manufacturer extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo", length = 80)
    private String logo;

}