package com.financiaPro.app.repository;
import com.financiaPro.app.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    List<LoanRequest> findByLenderId(Long lenderId);

}