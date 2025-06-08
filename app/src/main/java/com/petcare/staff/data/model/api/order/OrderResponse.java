package com.petcare.staff.data.model.api.order;

import java.util.List;

public class OrderResponse {
    private int order_id;
    private int customer_id;
    private int branch_id;
    private int appointment_id;
    private OrderStatus status;
    private String created_at;
    private List<OrderItemResponse> items;

    public OrderResponse(int order_id, int customer_id, int branch_id, int appointment_id,
                         OrderStatus status, String created_at, List<OrderItemResponse> items) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.status = status;
        this.created_at = created_at;
        this.items = items;
    }
// getters & setters

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}