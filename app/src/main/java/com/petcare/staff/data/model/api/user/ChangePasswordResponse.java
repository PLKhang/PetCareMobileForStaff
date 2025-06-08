package com.petcare.staff.data.model.api.user;

public class ChangePasswordResponse {
    /*
    {
        "status": "string"
    }
    */
    private String status;

    public ChangePasswordResponse(String status) {
        this.status = status;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}