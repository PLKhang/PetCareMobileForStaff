package com.petcare.staff.data.model.api.user;

public class LoginResponse {
/*
    {
        "status": "string",
        "token": "string"
    }
    */
    private String status;
    private String token;

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }
}

