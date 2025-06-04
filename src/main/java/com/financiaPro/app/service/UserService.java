package com.financiaPro.app.service;


import com.financiaPro.app.models.User;
import com.financiaPro.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        Optional<User> alreadyExisting = userRepository.findByEmail(user.getEmail());
        if (alreadyExisting.isPresent()) {
          throw new IllegalArgumentException("Email already taken");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> currentUser = userRepository.findById(id);
        if (currentUser.isEmpty()) {
            throw new IllegalArgumentException("User do not exist");
        }
        return currentUser.get();
    }



}
