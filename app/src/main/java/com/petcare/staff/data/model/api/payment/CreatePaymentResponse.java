package com.petcare.staff.data.model.api.payment;

public class CreatePaymentResponse {
/*
    {
        "payment_id": 0
    }
    */
    private int payment_id;

    public CreatePaymentResponse(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
