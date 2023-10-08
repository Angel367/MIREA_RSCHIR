package com.example.egor.Entities.Products;

import com.example.egor.Entities.AbstractProduct;
import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class WashingMachine extends AbstractProduct {
    private String manufacturer;

    private int tankCapacity;

    private int sellerNumber;

    private String productType;

    public WashingMachine() {
    }
}
