package com.example.backend.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends User {

    private int adminLevel;
    private String permissionCode;

    public AdminUser() {
        this.setRole(UserRole.ADMIN);
        this.adminLevel = 1;
    }

    // Polymorphism implementation
    @Override
    public String getRoleDescription() {
        return "Administrator user with clearance level " + adminLevel + ".";
    }

    // Encapsulation
    public int getAdminLevel() { return adminLevel; }
    public void setAdminLevel(int adminLevel) { this.adminLevel = adminLevel; }

    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
}
