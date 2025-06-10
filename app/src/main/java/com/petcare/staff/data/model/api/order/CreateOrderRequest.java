package com.petcare.staff.data.model.api.order;

import com.petcare.staff.utils.DateTime;

import java.util.List;

public class CreateOrderRequest {
    private int customer_id;
    private int branch_id;
    private int appointment_id;
    private List<OrderItemRequest> items;
    private String pickup_time;
    public CreateOrderRequest(int customer_id, int branch_id, int appointment_id, List<OrderItemRequest> items) {
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.items = items;
        this.pickup_time = new DateTime().toIsoString();
    }

    public CreateOrderRequest(int customer_id, int branch_id, int appointment_id, List<OrderItemRequest> items, String pickup_time) {
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.items = items;
        this.pickup_time = pickup_time;
    }

    // getters & setters

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
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

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
