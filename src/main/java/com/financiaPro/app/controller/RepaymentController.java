package com.financiaPro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financiaPro.app.models.Repayement;
import com.financiaPro.app.service.RepaymentService;

@RestController
@RequestMapping("api/repayments")
public class RepaymentController {

  @Autowired
  private RepaymentService repaymentService;

  @PostMapping("/loan/{loanRequestId}/repay")
  public ResponseEntity<Object> createRepayment(@PathVariable Long loanRequestId, @RequestHeader("X-API-KEY") String userApiKey, @RequestBody Repayement repayement) {
    try {
      Repayement repayment = repaymentService.createRepayement(loanRequestId, userApiKey, repayement);

      return new ResponseEntity<>(repayment, HttpStatus.ACCEPTED);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/loan/{loanRequestId}/repayments")
  public ResponseEntity<Object> getAllRepayments(@PathVariable Long loanRequestId) {
    try {
      List<Repayement> allRepayements = repaymentService.getAllRepayments(loanRequestId);

      return new ResponseEntity<>(allRepayements, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
