package com.example.backend.user.service;

import com.example.backend.user.dto.UpdateUserDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.model.AccountStatus;

import java.util.List;

// Abstraction: Interface hides implementation details
public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long userId);
    List<UserResponseDTO> searchUsers(String keyword, UserRole role, AccountStatus status);
    UserResponseDTO updateUserProfile(Long userId, UpdateUserDTO updateDTO);
    void changePassword(Long userId, String currentPassword, String newPassword);
    void updateAccountStatus(Long userId, AccountStatus status);
    void updateUserRole(Long userId, UserRole role);
    void deleteUser(Long userId);
}
