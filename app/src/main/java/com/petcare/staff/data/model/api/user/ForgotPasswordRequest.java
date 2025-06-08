package com.petcare.staff.data.model.api.user;

public class ForgotPasswordRequest {
    /*
    {
        "email": "string"
    }
    */
    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}