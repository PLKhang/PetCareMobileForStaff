package com.petcare.staff.data.model.api.payment;

public class UpdatePaymentAmountResponse {
/*
    {
        "status": "string"
    }
    */
    private String status;

    public UpdatePaymentAmountResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
