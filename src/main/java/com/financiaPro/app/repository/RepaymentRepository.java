package com.financiaPro.app.repository;
import com.financiaPro.app.models.Repayement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentRepository extends JpaRepository<Repayement, Long> {

  List<Repayement> findByLoanRequestId(Long loanRequestId);
}