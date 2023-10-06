package com.example.egor.Entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Telephone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String manufacturer;

    private int batteryCapacity;

    private String phoneNumber;

    private String productType;

    private String price;

    private String name;

    public Telephone() {
    }

    public Telephone(Long id, String manufacturer, int batteryCapacity, String phoneNumber,
                     String productType, String price, String name) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.batteryCapacity = batteryCapacity;
        this.phoneNumber = phoneNumber;
        this.productType = productType;
        this.price = price;
        this.name = name;
    }
}
