package com.financiaPro.app.service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import com.financiaPro.app.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRequestRepository loanrepository;

    public LoanRequest createLoanRequest(LoanRequest loanRequest) {
        return loanrepository.save(loanRequest);
    }

    public List<LoanRequest> getUserPendingLoanRequest (Long userId) {
        return loanrepository.findByLenderIdAndStatus(userId, LoanStatus.PENDING);
    }

    public List<LoanRequest> getUserHistoryLoanRequest(Long userId) {
        List<LoanStatus> statusList = List.of(LoanStatus.REFUSED, LoanStatus.FINISHED);
        return loanrepository.findByLenderIdAndStatusIn(userId.intValue(), statusList);
    }

    public LoanRequest acceptLoanRequest (Long loanRequestId) {
        Optional<LoanRequest> pendingLoanRequest = loanrepository.findById(loanRequestId);
        LoanRequest acceptedLoanRequest = pendingLoanRequest.get();
        acceptedLoanRequest.setStatus(LoanStatus.ON_GOING);
        loanrepository.save(acceptedLoanRequest);
        return acceptedLoanRequest;
    }

    public LoanRequest refuseLoanRequest (Long loanRequestId) {
        Optional<LoanRequest> pendingLoanRequest = loanrepository.findById(loanRequestId);
        LoanRequest acceptedLoanRequest = pendingLoanRequest.get();
        acceptedLoanRequest.setStatus(LoanStatus.REFUSED);
        loanrepository.save(acceptedLoanRequest);
        return acceptedLoanRequest;
    }



}
