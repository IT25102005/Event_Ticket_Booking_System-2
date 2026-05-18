package com.example.backend.user.service;

import com.example.backend.user.dto.UpdateUserDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.exception.UserNotFoundException;
import com.example.backend.user.model.User;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.model.AccountStatus;
import com.example.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return mapToDTO(user);
    }

    @Override
    public List<UserResponseDTO> searchUsers(String keyword, UserRole role, AccountStatus status) {
        List<User> users;
        
        if (keyword != null && !keyword.isEmpty()) {
            users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, keyword);
        } else if (role != null) {
            users = userRepository.findByRole(role);
        } else if (status != null) {
            users = userRepository.findByStatus(status);
        } else {
            users = userRepository.findAll();
        }
        
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserProfile(Long userId, UpdateUserDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (updateDTO.getFirstName() != null) user.setFirstName(updateDTO.getFirstName());
        if (updateDTO.getLastName() != null) user.setLastName(updateDTO.getLastName());
        if (updateDTO.getPhoneNumber() != null) user.setPhoneNumber(updateDTO.getPhoneNumber());
        if (updateDTO.getAddress() != null) user.setAddress(updateDTO.getAddress());
        if (updateDTO.getProfileImageUrl() != null) user.setProfileImageUrl(updateDTO.getProfileImageUrl());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateAccountStatus(Long userId, AccountStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public void updateUserRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        // In a real app we might need to recreate the entity to change DiscriminatorValue properly,
        // but for this simple JPA setup we update the role field. 
        // Note: JPA Single Table discriminator can be tricky to update directly without native queries.
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // Helper method mapping Entity to DTO (protects password)
    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Demonstrating Polymorphism
        dto.setRoleDescription(user.getRoleDescription());
        
        return dto;
    }
}
