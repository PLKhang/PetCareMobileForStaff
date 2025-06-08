package com.petcare.staff.data.model.api.payment;

public class UpdatePaymentMethodResponse {
    /*
    {
        "status": "string"
    }
    */
    private String status;

    public UpdatePaymentMethodResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

