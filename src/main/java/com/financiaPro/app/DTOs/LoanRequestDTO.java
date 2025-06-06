package com.financiaPro.app.DTOs;

import lombok.Data;

@Data
public class LoanRequestDTO {

    private Long lenderId;
    private Float amount;
    private Float interest;
    private int duration;
}
