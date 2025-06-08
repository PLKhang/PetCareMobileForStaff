package com.petcare.staff.data.model.api.user;

public class CustomerResponse {
/*
    {
        "address": "123 Main St, City",
        "email": "johndoe@example.com",
        "name": "John Doe",
        "phoneNumber": "+84912345678",
        "userId": 1001
    }
    */
    private Integer userId;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
