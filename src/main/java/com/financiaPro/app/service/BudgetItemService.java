package com.financiaPro.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.repository.BudgetItemRepository;


@Service
public class BudgetItemService {

    @Autowired
    private BudgetItemRepository budgetItemReposity;

    public BudgetItem createBudgetItem(BudgetItem budgetItem) {
        return budgetItemReposity.save(budgetItem);
    }

    public BudgetItem getBudgetItemById(Long id) {
        Optional<BudgetItem> budgetItem = budgetItemReposity.findById(id);

        if (budgetItem.isEmpty()) {
            throw new IllegalArgumentException("Error: budget item do not exist");
        }

        return budgetItem.get();
    }
}
