package com.financiaPro.app.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.models.BudgetType;
import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.BudgetItemRepository;
import com.financiaPro.app.repository.UserRepository;


@Service
public class BudgetItemService {

    @Autowired
    private BudgetItemRepository budgetItemReposity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public BudgetItem createBudgetItem(BudgetItem budgetItem) {
        Optional<User> user = userRepository.findById(budgetItem.getUser_id());

        if (!user.isPresent()) {
            throw new IllegalArgumentException("Error: User does not exist");
        }

        if (budgetItem.getType() == BudgetType.INCOME) {
            Float newRevenue = user.get().getRevenues() + budgetItem.getAmount();
            user.get().setRevenues(newRevenue);
            user.get().setBalance(user.get().getBalance() + budgetItem.getAmount());
            userService.updateUser(user.get());
        } else if (budgetItem.getType() == BudgetType.EXPENSE) {
            Float newExpense = user.get().getExpenses() + budgetItem.getAmount();
            user.get().setExpenses(newExpense);
            user.get().setBalance(user.get().getBalance() - budgetItem.getAmount());
            userService.updateUser(user.get());
        }

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
