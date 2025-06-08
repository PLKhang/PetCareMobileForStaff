package com.petcare.staff.data.model.api.user;

public class ChangeInfoResponse {
    /*
    {
        "address": "string",
        "email": "string",
        "name": "string",
        "phoneNumber": "string",
        "status": "string"
    }
    */
    private String address;
    private String email;
    private String name;
    private String phoneNumber;
    private String status;

    public ChangeInfoResponse(String address, String email, String name, String phoneNumber, String status) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}