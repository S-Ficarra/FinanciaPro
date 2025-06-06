package com.financiaPro.app.service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRequestRepository loanrepository;

    public LoanRequest createLoanRequest(LoanRequest loanRequest) {
        return loanrepository.save(loanRequest);
    }

    public List<LoanRequest> getUserAllLoanRequest (Long userId) {
        return loanrepository.findByLenderId(userId);
    }

}
