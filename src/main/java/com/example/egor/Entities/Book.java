package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    private String author;

    private int sellerNumber;

    private String productType;

    private double price;

    private String title;

    public Book() {
    }
}
