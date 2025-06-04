package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private int borrowerId;
    private int lenderId;
    private Float amount;
    private Float interest;
    private int duration;
    private LoanStatus status;

}