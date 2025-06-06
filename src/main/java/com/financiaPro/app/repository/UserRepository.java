package com.financiaPro.app.repository;
import com.financiaPro.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByApiKey(String apiKey);


}