package com.financiaPro.app.service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
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

    public List<LoanRequest> getUserPendingLoanRequest (Long userId) {
        return loanrepository.findByBorrowerIdAndStatus(userId, LoanStatus.PENDING);
    }

    public List<LoanRequest> getUserHistoryLoanRequest(Long userId) {
        List<LoanStatus> statusList = List.of(LoanStatus.REFUSED, LoanStatus.FINISHED);
        return loanrepository.findByBorrowerIdAndStatusIn(userId.intValue(), statusList);
    }

}
