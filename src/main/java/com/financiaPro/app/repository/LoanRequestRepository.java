package com.financiaPro.app.repository;
import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    List<LoanRequest> findByLenderIdAndStatus(Long lenderId, LoanStatus status);
    List<LoanRequest> findByLenderIdAndStatusIn(int lenderId, List<LoanStatus> statusList);
    List<LoanRequest> findByBorrowerIdAndStatus(long borrowerID, LoanStatus status);
}