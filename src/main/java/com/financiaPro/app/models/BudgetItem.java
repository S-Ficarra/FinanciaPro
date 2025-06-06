package com.financiaPro.app.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private BudgetType type;
    private Float amount;
    private String description;
    private Date date;
    private Long user_id;

}