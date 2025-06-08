package com.petcare.staff.data.model.api.payment;

public class CreatePaymentUrlRequest {
    /*
        {
            "amount": 0,
                "description": "string",
                "payment_id": "string"
        }
        */
    public float amount;
    public String description;
    public String payment_id;

    public CreatePaymentUrlRequest(float amount, String description, String payment_id) {
        this.amount = amount;
        this.description = description;
        this.payment_id = payment_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
