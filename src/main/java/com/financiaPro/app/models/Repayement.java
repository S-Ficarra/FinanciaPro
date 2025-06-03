package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
public class Repayement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private int loanRequestId;
    private Float amount;
    private Date date;
    private String comment;

}