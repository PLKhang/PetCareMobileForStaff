package com.petcare.staff.data.model.api.payment;

public class CancelPaymentRequest {
    private String cancellation_reason;
    private String payment_id;

    public CancelPaymentRequest() {
    }

    public CancelPaymentRequest(String cancellation_reason, String payment_id) {
        this.cancellation_reason = cancellation_reason;
        this.payment_id = payment_id;
    }

    public String getCancellation_reason() {
        return cancellation_reason;
    }

    public void setCancellation_reason(String cancellation_reason) {
        this.cancellation_reason = cancellation_reason;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
