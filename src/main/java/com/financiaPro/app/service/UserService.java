package com.financiaPro.app.service;

import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public Map<String, String> getUserMe(String apiKey) {

        Optional<User> user = userRepository.findByApiKey(apiKey);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Error: User do not exist");
        }

        String firstName = user.get().getFirstName();
        String name = user.get().getName();
        String email = user.get().getEmail();

        Map<String, String> result = new HashMap<>();
        result.put("First name", firstName);
        result.put("Last name", name);
        result.put("Email", email);

        return result;
    }
}
