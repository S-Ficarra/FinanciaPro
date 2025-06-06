package com.financiaPro.app.service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    private LoanRequestRepository loanrepository;

    public LoanRequest createLoanRequest(LoanRequest loanRequest) {

        return loanrepository.save(loanRequest);

    }

}
