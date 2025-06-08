package com.petcare.staff.data.model.api.order;

public class UpdateOrderStatusResponse {
    private String status;

    public UpdateOrderStatusResponse(String status) {
        this.status = status;
    }
// getters & setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}