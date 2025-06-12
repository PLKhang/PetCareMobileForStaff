package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.payment.PaymentMethod;
import com.petcare.staff.data.model.api.payment.PaymentStatus;

public class Bill {
    private String id;
    private String orderId;
    private String appointmentId;
    private float amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String description;
    private String paymentUrl;

    public Bill(String orderId, String appointmentId, float amount, PaymentMethod paymentMethod, String description) {
        this.orderId = orderId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }

    public Bill(String id, String orderId, String appointmentId, float amount,
                PaymentMethod paymentMethod, PaymentStatus status, String description) {
        this.id = id;
        this.orderId = orderId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.description = description;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
