package com.petcare.staff.data.model.api.payment;

public class UpdatePaymentStatusRequest {
    /*
        {
            "payment_id": "string",
                "status": "string"
        }
        */
    public String payment_id;
    public PaymentStatus status;

    public UpdatePaymentStatusRequest(String payment_id, PaymentStatus status) {
        this.payment_id = payment_id;
        this.status = status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
