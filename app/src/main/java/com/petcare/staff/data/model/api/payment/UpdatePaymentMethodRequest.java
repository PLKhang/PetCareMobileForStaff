package com.petcare.staff.data.model.api.payment;

public class UpdatePaymentMethodRequest {
    /*
    {
        "method": "string",
            "payment_id": "string"
    }*/
    public PaymentMethod method;
    public String payment_id;

    public UpdatePaymentMethodRequest(PaymentMethod method, String payment_id) {
        this.method = method;
        this.payment_id = payment_id;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
