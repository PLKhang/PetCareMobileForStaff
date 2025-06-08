package com.petcare.staff.data.model.api.user;

import com.google.gson.annotations.SerializedName;

public class UserByIdResponse {
/*
    {
        "ID": 4,
            "Email": "pkstranger2003@gmail.com",
            "Name": "0987654321",
            "Password": "$2a$10$Y7VC212sjKnvGu0UHNff5OcGtFzddqWWdXFh5GesDfGRz032s4wU2",
            "Address": "Lê Phúc Khang",
            "PhoneNumber": "Huhu",
            "CreatedAt": {
        "seconds": 1748746496,
                "nanos": 187000000
    }
    }*/

    @SerializedName("ID")
    private int id;

    @SerializedName("Email")
    private String email;

    @SerializedName("Name")
    private String name;

    @SerializedName("Password")
    private String password;

    @SerializedName("Address")
    private String address;

    @SerializedName("PhoneNumber")
    private String phoneNumber;

    public UserByIdResponse(int id, String email, String name, String password, String address, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
}

