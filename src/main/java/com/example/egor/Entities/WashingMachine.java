package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class WashingMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "washing_machine_seq")
    @SequenceGenerator(name = "washing_machine_seq", sequenceName = "washing_machine_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
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
