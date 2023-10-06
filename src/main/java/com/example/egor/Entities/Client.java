package com.example.egor.Entities;

import lombok.*;
import jakarta.persistence.*;

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

    public Client(Long id, String name, String email, String login, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
    }
}
