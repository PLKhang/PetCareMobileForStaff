package com.petcare.staff.data.model.api.payment;

public class CreatePaymentRequest {
/*
    {
        "amount": 0,
            "appointment_id": 0,
            "description": "string",
            "method": "string",
            "order_id": 0
    }
    */
    public float amount;
    public int appointment_id;
    public String description;
    public PaymentMethod method;
    public int order_id;

    public CreatePaymentRequest(float amount, int appointment_id, String description, PaymentMethod method, int order_id) {
        this.amount = amount;
        this.appointment_id = appointment_id;
        this.description = description;
        this.method = method;
        this.order_id = order_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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
}
