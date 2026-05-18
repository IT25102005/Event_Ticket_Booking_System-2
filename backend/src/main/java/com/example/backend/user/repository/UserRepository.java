package com.example.backend.user.repository;

import com.example.backend.user.model.User;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    // For search functionality
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email);
            
    List<User> findByRole(UserRole role);
    List<User> findByStatus(AccountStatus status);
}
