package com.example.egor.Entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Data
@Getter @Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String email;

    private String login;

    private String password;

    public Client() {
    }
}
