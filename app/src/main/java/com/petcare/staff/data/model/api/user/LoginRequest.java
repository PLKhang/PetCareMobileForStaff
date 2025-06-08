package com.petcare.staff.data.model.api.user;

public class LoginRequest {
    /*
    {
      "email": "string",
      "password": "string",
      "rememberMe": true
    }
    */
    private String email;
    private String password;
    private boolean rememberMe;

    public LoginRequest(String email, String password, boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }
}

