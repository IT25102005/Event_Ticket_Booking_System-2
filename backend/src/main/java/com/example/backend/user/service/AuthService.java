package com.example.backend.user.service;

import com.example.backend.user.dto.LoginRequestDTO;
import com.example.backend.user.dto.RegisterRequestDTO;
import com.example.backend.user.dto.UserResponseDTO;
import com.example.backend.user.model.UserRole;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO registerDTO, UserRole role);
    UserResponseDTO login(LoginRequestDTO loginDTO);
}
