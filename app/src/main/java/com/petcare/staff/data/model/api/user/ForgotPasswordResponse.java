package com.petcare.staff.data.model.api.user;

public class ForgotPasswordResponse {
    /*
    {
        "message": "string"
    }
    */
    private String message;

    public ForgotPasswordResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}