package com.petcare.staff.data.model.api.payment;

public class CreatePaymentUrlResponse {
/*
    {
        "checkout_url": "string",
            "payment_link_id": "string"
    }
    */
    public String checkout_url;
    public String payment_link_id;

    public CreatePaymentUrlResponse(String checkout_url, String payment_link_id) {
        this.checkout_url = checkout_url;
        this.payment_link_id = payment_link_id;
    }

    public String getCheckout_url() {
        return checkout_url;
    }

    public void setCheckout_url(String checkout_url) {
        this.checkout_url = checkout_url;
    }

    public String getPayment_link_id() {
        return payment_link_id;
    }

    public void setPayment_link_id(String payment_link_id) {
        this.payment_link_id = payment_link_id;
    }
}
