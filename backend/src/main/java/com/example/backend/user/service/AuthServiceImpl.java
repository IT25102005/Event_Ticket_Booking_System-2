package com.example.backend.user.service;

import com.example.backend.user.dto.LoginRequestDTO;
import com.example.backend.user.dto.RegisterRequestDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.exception.DuplicateEmailException;
import com.example.backend.user.exception.InvalidLoginException;
import com.example.backend.user.model.AdminUser;
import com.example.backend.user.model.CustomerUser;
import com.example.backend.user.model.User;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO registerDTO, UserRole role) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new DuplicateEmailException("Email is already taken");
        }
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new DuplicateEmailException("Username is already taken");
        }

        // Creating the specific subclass based on role
        User user;
        if (role == UserRole.ADMIN) {
            user = new AdminUser();
        } else {
            user = new CustomerUser();
        }

        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setAddress(registerDTO.getAddress());
        user.setProfileImageUrl(registerDTO.getProfileImageUrl());

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO loginDTO) {
        String identifier = loginDTO.getIdentifier();
        
        // Find by email or username
        User user = userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByUsername(identifier)
                        .orElseThrow(() -> new InvalidLoginException("Invalid credentials")));

        // Verify password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidLoginException("Invalid credentials");
        }

        return mapToDTO(user);
    }

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
        dto.setRoleDescription(user.getRoleDescription());
        return dto;
    }
}
