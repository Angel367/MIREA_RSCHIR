package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

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
}
