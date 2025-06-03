package com.financiaPro.app.repository;
import com.financiaPro.app.models.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {


}