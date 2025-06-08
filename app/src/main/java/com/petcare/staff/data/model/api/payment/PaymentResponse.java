package com.petcare.staff.data.model.api.payment;

public class PaymentResponse {
    /*
    {
        "amount": 0,
        "appointment_id": 0,
        "description": "string",
        "method": "string",
        "order_id": 0,
        "payment_id": 0,
        "status": "string"
    }
    */
    private int amount;
    private int appointment_id;
    private String description;
    private PaymentMethod method;
    private int order_id;
    private int payment_id;
    private PaymentStatus status;

    public PaymentResponse(int amount, int appointment_id, String description, PaymentMethod method, int order_id, int payment_id, PaymentStatus status) {
        this.amount = amount;
        this.appointment_id = appointment_id;
        this.description = description;
        this.method = method;
        this.order_id = order_id;
        this.payment_id = payment_id;
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
