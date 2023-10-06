package com.example.egor.Entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class WashingMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String manufacturer;

    private int tankCapacity;

    private int sellerNumber;

    private String productType;

    private String price;

    private String name;

    public WashingMachine() {
    }

    public WashingMachine(Long id, String manufacturer, int tankCapacity, int sellerNumber,
                          String productType, String price, String name) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.tankCapacity = tankCapacity;
        this.sellerNumber = sellerNumber;
        this.productType = productType;
        this.price = price;
        this.name = name;
    }
}
