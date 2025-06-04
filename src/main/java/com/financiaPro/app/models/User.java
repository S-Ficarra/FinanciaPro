package com.financiaPro.app.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Float balance;
    private Float revenues;
    private Float expenses;
}