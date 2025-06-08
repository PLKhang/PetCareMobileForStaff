package com.petcare.staff.data.model.api.user;

public class ChangeInfoRequest {
    /*
    {
        "address": "string",
        "email": "string",
        "name": "string",
        "phoneNumber": "string"
    }
    */
    private String address;
    private String email;
    private String name;
    private String phoneNumber;

    public ChangeInfoRequest(String address, String email, String name, String phoneNumber) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}