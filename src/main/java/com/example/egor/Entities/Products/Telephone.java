package com.example.egor.Entities.Products;

import com.example.egor.Entities.AbstractProduct;
import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Telephone extends AbstractProduct {
    private String manufacturer;

    private int batteryCapacity;

    private String phoneNumber;

    private String productType;


    public Telephone() {
    }
}
