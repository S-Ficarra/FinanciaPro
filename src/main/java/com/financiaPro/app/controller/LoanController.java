package com.financiaPro.app.controller;

import com.financiaPro.app.DTOs.LoanRequestDTO;
import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import com.financiaPro.app.models.User;
import com.financiaPro.app.service.LoanService;
import com.financiaPro.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private UserService userService;


    @PostMapping("/request")
    public ResponseEntity<Object> requestLoan (@RequestBody LoanRequestDTO loanRequestDTO, @RequestHeader("X-API-KEY") String apiKey) {

        try {
            User requestingUser = userService.getUserByApiKey(apiKey);

            LoanRequest loanRequest = new LoanRequest();

            loanRequest.setBorrowerId(requestingUser.getId());
            loanRequest.setLenderId(loanRequestDTO.getLenderId());
            loanRequest.setBorrowerId(loanRequestDTO.getBorrowerId());
            loanRequest.setAmount(loanRequestDTO.getAmount());
            loanRequest.setDuration(loanRequestDTO.getDuration());
            loanRequest.setInterest(loanRequestDTO.getInterest());
            loanRequest.setStatus(LoanStatus.PENDING);

            LoanRequest newLoanRequest = loanService.createLoanRequest(loanRequest);
            return new ResponseEntity<>(newLoanRequest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/incoming")
    public ResponseEntity<Object> getLoanRequests (@RequestHeader("X-API-KEY") String apiKey) {

        try {
            User user = userService.getUserByApiKey(apiKey);
            List<LoanRequest> allLoanRequests = loanService.getUserPendingLoanRequest(user.getId());

            return new ResponseEntity<>(allLoanRequests, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getHistoryLoanRequest (@RequestHeader("X-API-KEY") String apiKey) {

        try {
            User user = userService.getUserByApiKey(apiKey);
            List<LoanRequest> allLoanRequests = loanService.getUserHistoryLoanRequest(user.getId());

            return new ResponseEntity<>(allLoanRequests, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Object> acceptLoanRequest (@PathVariable Long id, @RequestHeader("X-API-KEY") String apiKey) {

        try {          
            LoanRequest acceptedLoanRequest = loanService.acceptLoanRequest(id, apiKey);
            User borrower = userService.getUserById(acceptedLoanRequest.getBorrowerId());

            if (acceptedLoanRequest.getAmount() > borrower.getRevenues() * 2) {
                return new ResponseEntity<>("Loan request accepted. However, the borrower's revenues are low.", HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<>(acceptedLoanRequest, HttpStatus.ACCEPTED);                
            }

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/refuse")
    public ResponseEntity<Object> refuseLoanRequest (@PathVariable Long id, @RequestHeader("X-API-KEY") String apiKey) {

        try {
            LoanRequest acceptedLoanRequest = loanService.refuseLoanRequest(id, apiKey);
            return new ResponseEntity<>(acceptedLoanRequest, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
