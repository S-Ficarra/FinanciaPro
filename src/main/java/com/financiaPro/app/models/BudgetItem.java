package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private BudgetType type;
    private Float amount;
    private String description;
    private Date date;
    private int user_Id;

}