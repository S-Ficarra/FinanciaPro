package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String firstName;
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String apiKey;

}