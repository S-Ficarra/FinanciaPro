package com.financiaPro.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import com.financiaPro.app.models.Repayement;
import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.LoanRequestRepository;
import com.financiaPro.app.repository.RepaymentRepository;


@Service
public class RepaymentService {

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired LoanRequestRepository loanRequestRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    public Repayement createRepayement(Long loanRequestId, String userApiKey, Repayement repayement) {
        Optional<LoanRequest> loanRequest = loanRequestRepository.findById(loanRequestId);
        User borrower = userService.getUserById(loanRequest.get().getBorrowerId());
        String borrowerApiKey = borrower.getApiKey();

        Boolean isLoanRequestAccepted = loanRequest.get().getStatus().equals(LoanStatus.ON_GOING);
        Boolean isBorrower = borrowerApiKey.equals(userApiKey);

        if (loanRequest.isPresent() && isLoanRequestAccepted) {
            if (isBorrower) {
                Float currentAmount = loanRequest.get().getAmount();
                Float repaymentAmount = repayement.getAmount();

                loanRequest.get().setAmount(currentAmount - repaymentAmount);
                loanService.updateLoanRequest(loanRequest.get());
                repayement.setLoanRequestId(loanRequestId);
                
                return repaymentRepository.save(repayement);
            } else {
                throw new RuntimeException("This user is not authorized to make payments.");
            }
        } else {
            throw new RuntimeException("Loan request was not accepted.");
        }
    }

    public List<Repayement> getAllRepayments(Long loanRequestId)  {
        return repaymentRepository.findByLoanRequestId(loanRequestId);
    }
}
