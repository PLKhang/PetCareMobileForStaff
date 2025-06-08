package com.petcare.staff.data.model.api.user;

public class UserResponse {
/*
    {
        "address": "string",
        "email": "string",
        "id": 0,
        "name": "string",
        "phoneNumber": "string"
    }
    */
    private Integer id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
