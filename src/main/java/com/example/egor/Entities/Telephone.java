package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Telephone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telephone_seq")
    @SequenceGenerator(name = "telephone_seq", sequenceName = "telephone_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
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
