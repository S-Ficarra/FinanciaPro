package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
public class Repayement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long loanRequestId;
    private Float amount;
    private Date date;
    private String comment;

}