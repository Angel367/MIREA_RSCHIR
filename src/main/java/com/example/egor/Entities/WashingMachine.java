package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

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
}
