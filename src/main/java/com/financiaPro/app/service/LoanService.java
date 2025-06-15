package com.financiaPro.app.service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRequestRepository loanrepository;

    @Autowired
    private UserService userService;

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

    public LoanRequest acceptLoanRequest (Long loanRequestId, String userApiKey) {

        Optional<LoanRequest> pendingLoanRequest = loanrepository.findById(loanRequestId);
        User lender = userService.getUserById(pendingLoanRequest.get().getLenderId());
        String lenderApiKey = lender.getApiKey();

        User borrower = userService.getUserById(pendingLoanRequest.get().getBorrowerId());

        if (!lenderApiKey.equals(userApiKey)) {
            throw new RuntimeException("Only the lender can accept a loan request");
        }

        if (lender.getBalance() < 500.0) {
            throw new RuntimeException("Sorry, this user's amount is less than 500");
        }

        LoanRequest acceptedLoanRequest = pendingLoanRequest.get();
        acceptedLoanRequest.setStatus(LoanStatus.ON_GOING);
        loanrepository.save(acceptedLoanRequest);

        lender.setBalance(lender.getBalance() - acceptedLoanRequest.getAmount());
        userService.updateUser(lender);

        borrower.setBalance(borrower.getBalance() + acceptedLoanRequest.getAmount());
        userService.updateUser(borrower);

        return acceptedLoanRequest;
    }

    public LoanRequest refuseLoanRequest (Long loanRequestId, String userApiKey) {

        Optional<LoanRequest> pendingLoanRequest = loanrepository.findById(loanRequestId);
        User lender = userService.getUserById(pendingLoanRequest.get().getLenderId());
        String lenderApiKey = lender.getApiKey();

        if (!lenderApiKey.equals(userApiKey)) {
            throw new RuntimeException("Only the lender can refuse a loan request");
        }

        LoanRequest acceptedLoanRequest = pendingLoanRequest.get();
        acceptedLoanRequest.setStatus(LoanStatus.REFUSED);
        loanrepository.save(acceptedLoanRequest);
        return acceptedLoanRequest;
    }

    public LoanRequest updateLoanRequest(LoanRequest loanRequestUpdated)  {
        Optional<LoanRequest> currentLoanRequest = loanrepository.findById(loanRequestUpdated.getId());
        
        if (!currentLoanRequest.isPresent()) {
            throw new RuntimeException("This loan request does not exist");
        }
    
        LoanRequest updatedLoanRequest = currentLoanRequest.get();
        updatedLoanRequest.setAmount(loanRequestUpdated.getAmount());
        
        return loanrepository.save(updatedLoanRequest); 
    }
}
