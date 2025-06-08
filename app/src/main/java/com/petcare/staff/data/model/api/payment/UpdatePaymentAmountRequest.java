package com.petcare.staff.data.model.api.payment;

public class UpdatePaymentAmountRequest {
    /*
        {
            "amount": 0,
                "payment_id": "string"
        }
        */
    public float amount;
    public String payment_id;

    public UpdatePaymentAmountRequest(float amount, String payment_id) {
        this.amount = amount;
        this.payment_id = payment_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
