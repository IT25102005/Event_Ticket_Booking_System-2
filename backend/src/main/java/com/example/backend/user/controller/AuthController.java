package com.example.backend.user.controller;

import com.example.backend.user.dto.LoginRequestDTO;
import com.example.backend.user.dto.RegisterRequestDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.model.UserRole;
import com.example.backend.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // For development purposes
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerCustomer(@Valid @RequestBody RegisterRequestDTO registerDTO) {
        UserResponseDTO response = authService.register(registerDTO, UserRole.CUSTOMER);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody RegisterRequestDTO registerDTO) {
        UserResponseDTO response = authService.register(registerDTO, UserRole.ADMIN);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        UserResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
}
