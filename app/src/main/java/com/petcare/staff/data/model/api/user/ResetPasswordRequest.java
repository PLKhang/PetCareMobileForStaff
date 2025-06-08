package com.petcare.staff.data.model.api.user;

public class ResetPasswordRequest {
    /*
    {
        "newPassword": "string",
        "token": "string",
        "userId": "string"
    }
    */
    private String newPassword;
    private String token;
    private String userId;

    public ResetPasswordRequest(String newPassword, String token, String userId) {
        this.newPassword = newPassword;
        this.token = token;
        this.userId = userId;
    }

    // Getters and Setters
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}