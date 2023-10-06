package com.example.egor.Entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String author;

    private int sellerNumber;

    private String productType;

    private double price;

    private String title;

    public Book() {

    }

    public Book(Long id, String author, int sellerNumber, String productType, double price, String title) {
        this.id = id;
        this.author = author;
        this.sellerNumber = sellerNumber;
        this.productType = productType;
        this.price = price;
        this.title = title;
    }
}