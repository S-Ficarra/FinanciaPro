package com.financiaPro.app.repository;
import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    List<LoanRequest> findByBorrowerIdAndStatus(Long borrowerId, LoanStatus status);
    List<LoanRequest> findByBorrowerIdAndStatusIn(int borrowerId, List<LoanStatus> statusList);
}