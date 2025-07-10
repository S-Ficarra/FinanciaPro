package com.financiaPro.app.service;

import com.financiaPro.app.models.BudgetItem;
import com.financiaPro.app.models.BudgetType;
import com.financiaPro.app.models.LoanRequest;
import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.UserRepository;

import java.security.SecureRandom;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private LoanService loanService;
    private BudgetItemService budgetItemService;

    public static String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public User createUser(User user) {

        user.setApiKey(generateApiKey());

        Optional<User> alreadyExisting = userRepository.findByEmail(user.getEmail());

        if (alreadyExisting.isPresent()) {
            throw new IllegalArgumentException("Error: Email already taken");
        }

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> userToUpdate = userRepository.findById(user.getId());

        if (!userToUpdate.isPresent()) {
            throw new IllegalArgumentException("Error: User does not exist");
        }

        userToUpdate.get().setFirstName(user.getFirstName());
        userToUpdate.get().setName(user.getName());
        userToUpdate.get().setEmail(user.getEmail());
        userToUpdate.get().setApiKey(user.getApiKey());
        userToUpdate.get().setBalance(user.getBalance());
        userToUpdate.get().setRevenues(user.getRevenues());
        userToUpdate.get().setExpenses(user.getExpenses());

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> currentUser = userRepository.findById(id);

        if (currentUser.isEmpty()) {
            throw new IllegalArgumentException("Error: User do not exist");
        }

        return currentUser.get();
    }

    public User getUserByEmail(String email) {
        Optional<User> currentUser = userRepository.findByEmail(email);

        if (currentUser.isEmpty()) {
            throw new IllegalArgumentException("Error: User do not exist");
        }

        return currentUser.get();
    }

    public User getUserByApiKey(String apiKey) {
        Optional<User> user = userRepository.findByApiKey(apiKey);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Error: User do not exist");
        }
        return user.get();
    }


    public Map<String, Float> getSummary(String apiKey) {

        Optional<User> user = userRepository.findByApiKey(apiKey);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Error: User do not exist");
        }

        Float revenue = user.get().getRevenues();
        Float expenses = user.get().getExpenses();
        Float balance = user.get().getBalance();

        Map<String, Float> result = new HashMap<>();
        result.put("revenue", revenue);
        result.put("expenses", expenses);
        result.put("balance", balance);

        return result;
    }

    private boolean isCreditTooHigh(User user) {
        Float totalCredit = loanService.getUserOnGoingLoanRequest(user.getId());

        List<BudgetItem> incomeItems = budgetItemService.getBudgetItems(null, BudgetType.INCOME, null);

        Float totalIncome = incomeItems.stream()
                .filter(item -> item.getUser_id().equals(user.getId()))
                .map(BudgetItem::getAmount)
                .filter(Objects::nonNull)
                .reduce(0f, Float::sum);

        return totalCredit > totalIncome * 2;
    }

    public Map<String, String> getUserMe(String apiKey) {
        Optional<User> userOpt = userRepository.findByApiKey(apiKey);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Error: User does not exist");
        }

        User user = userOpt.get();

        Map<String, String> result = new HashMap<>();
        result.put("First name", user.getFirstName());
        result.put("Last name", user.getName());
        result.put("Email", user.getEmail());

        if (isCreditTooHigh(user)) {
            result.put("Warning", "Warning!! your credit is too high!!");
        }
        return result;
    }
}
