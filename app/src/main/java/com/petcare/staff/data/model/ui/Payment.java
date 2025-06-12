package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.payment.PaymentMethod;

public class Payment {
    private String id;
    private String appointmentId;
    private String orderId;
    private PaymentMethod method;
    private String description;
    private float amount;

    public Payment(String id, String appointmentId, String orderId, PaymentMethod method, String description, float amount) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.orderId = orderId;
        this.method = method;
        this.description = description;
        this.amount = amount;
    }

    public Payment(String appointmentId, String orderId, PaymentMethod method,
                   String description, float amount) {
        this.appointmentId = appointmentId;
        this.orderId = orderId;
        this.method = method;
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
