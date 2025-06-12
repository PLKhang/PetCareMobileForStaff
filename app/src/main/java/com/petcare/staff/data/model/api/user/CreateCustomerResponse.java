package com.petcare.staff.data.model.api.user;

public class CreateCustomerResponse {
    int userId;

    public CreateCustomerResponse(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
