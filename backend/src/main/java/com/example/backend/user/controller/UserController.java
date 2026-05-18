package com.example.backend.user.controller;

import com.example.backend.user.dto.ChangePasswordDTO;
import com.example.backend.user.dto.UpdateUserDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.model.AccountStatus;
import com.example.backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) AccountStatus status) {
        return ResponseEntity.ok(userService.searchUsers(keyword, role, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserProfile(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserDTO updateDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(id, updateDTO));
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id, 
            @Valid @RequestBody ChangePasswordDTO passwordDTO) {
        userService.changePassword(id, passwordDTO.getCurrentPassword(), passwordDTO.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        userService.updateAccountStatus(id, AccountStatus.INACTIVE);
        return ResponseEntity.ok("User account deactivated");
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long id) {
        userService.updateAccountStatus(id, AccountStatus.ACTIVE);
        return ResponseEntity.ok("User account activated");
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestParam UserRole role) {
        userService.updateUserRole(id, role);
        return ResponseEntity.ok("User role updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
