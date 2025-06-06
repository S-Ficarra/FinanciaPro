package com.financiaPro.app.service;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.models.BudgetType;
import com.financiaPro.app.repository.BudgetItemRepository;


@Service
public class BudgetItemService {

    @Autowired
    private BudgetItemRepository budgetItemReposity;

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public BudgetItem createBudgetItem(BudgetItem budgetItem) {
        return budgetItemReposity.save(budgetItem);
    }

    public List<BudgetItem> getBudgetItems(Date date, BudgetType type, Float amount) {
        List<BudgetItem> budgetItems = budgetItemReposity.findAll();

        if (budgetItems.isEmpty()) {
            throw new IllegalArgumentException("Error: No budget items");
        }

        if (date != null && type != null && amount != null) {
            budgetItems = budgetItems
            .stream()
            .filter(item -> isSameDay(item.getDate(), date))
            .filter(item -> item.getType().equals(type))
            .filter(item -> item.getAmount().equals(amount))
            .collect(Collectors.toList());
        } else if (date != null && type != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> isSameDay(item.getDate(), date))
            .filter(item -> item.getType().equals(type))
            .collect(Collectors.toList());
        } else if (date != null && amount != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> isSameDay(item.getDate(), date))
            .filter(item -> item.getAmount().equals(amount))
            .collect(Collectors.toList());
        } else if (type != null &&  amount != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> item.getType().equals(type))
            .filter(item -> item.getAmount().equals(amount))
            .collect(Collectors.toList());
        } else if (date != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> isSameDay(item.getDate(), date))
            .collect(Collectors.toList());
        } else if (type != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> item.getType().equals(type))
            .collect(Collectors.toList());
        } else if (amount != null) {
            budgetItems = budgetItems
            .stream ()
            .filter(item -> item.getAmount().equals(amount))
            .collect(Collectors.toList());
        }

        return budgetItems;
    }

    public BudgetItem getBudgetItemById(Long id) {
        Optional<BudgetItem> budgetItem = budgetItemReposity.findById(id);

        if (budgetItem.isEmpty()) {
            throw new IllegalArgumentException("Error: budget item do not exist");
        }

        return budgetItem.get();
    }

    public String deleteBudgetItem(Long id) {
        Optional<BudgetItem> budgetItem = budgetItemReposity.findById(id);

        if (budgetItem.isEmpty()) {
            throw new IllegalArgumentException("Error: budget item do not exist");
        } else {
            budgetItemReposity.delete(budgetItem.get());
            return "Budget Item deleted";
        }
    }
}
