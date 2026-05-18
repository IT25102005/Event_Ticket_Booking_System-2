package com.example.backend.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class CustomerUser extends User {

    private int loyaltyPoints;
    private String preferredCategory;

    public CustomerUser() {
        this.setRole(UserRole.CUSTOMER);
        this.loyaltyPoints = 0;
    }

    // Polymorphism implementation
    @Override
    public String getRoleDescription() {
        return "Customer user with " + loyaltyPoints + " loyalty points.";
    }

    // Encapsulation
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public String getPreferredCategory() { return preferredCategory; }
    public void setPreferredCategory(String preferredCategory) { this.preferredCategory = preferredCategory; }
}
