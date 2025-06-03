package com.financiaPro.app.repository;
import com.financiaPro.app.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {


}