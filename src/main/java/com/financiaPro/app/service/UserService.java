package com.financiaPro.app.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public static String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
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
}
