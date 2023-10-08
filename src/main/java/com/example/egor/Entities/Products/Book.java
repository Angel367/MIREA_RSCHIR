package com.example.egor.Entities.Products;

import com.example.egor.Entities.AbstractProduct;
import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Book extends AbstractProduct {
    private String author;

    private int sellerNumber;

    private String productType;

    public Book() {
    }
}
