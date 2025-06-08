package com.petcare.staff.data.model.api.user;

import com.google.gson.annotations.SerializedName;

import kotlinx.serialization.Serializable;

public class UserResponse {
/*
    {
        "address": "string",
        "branchId": int
        "email": "string",
        "id": 0,
        "name": "string",
        "phoneNumber": "string"
    }
    */
    @SerializedName("userId")
    private Integer id;
    private int branchId;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public UserResponse(Integer id, int branchId, String name, String email, String address, String phoneNumber) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

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
