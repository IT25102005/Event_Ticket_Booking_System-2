package com.example.backend.user.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "Email or Username is required")
    private String identifier; // can be email or username

    @NotBlank(message = "Password is required")
    private String password;

    // Getters and Setters
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
