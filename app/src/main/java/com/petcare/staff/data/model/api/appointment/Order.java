package com.petcare.staff.data.model.api.appointment;

import java.util.List;

public class Order {
    private int id;
    private int customer_id;
    private int branch_id;
    private float total_price;
    private String created_at;
    private String updated_at;
    private List<OrderItem> items;
    private int appointment_id;

    public Order(int id, int customer_id, int branch_id, float total_price, String created_at, String updated_at, List<OrderItem> items, int appointment_id) {
        this.id = id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.total_price = total_price;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.items = items;
        this.appointment_id = appointment_id;
    }
    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }
}
