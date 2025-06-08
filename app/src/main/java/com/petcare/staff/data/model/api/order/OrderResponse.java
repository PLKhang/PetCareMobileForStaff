package com.petcare.staff.data.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {
    @SerializedName("id")
    private int id;
    private int customer_id;
    private int branch_id;
    @SerializedName("appointment_id")
    private Integer appointment_id;
    private float total_price;
    private String created_at;
    private String updated_at;
    private List<OrderItemResponse> items;

    public OrderResponse(int id, int customer_id, int branch_id, Integer appointment_id,
                         float total_price, String created_at, String updated_at, List<OrderItemResponse> items) {
        this.id = id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.total_price = total_price;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.items = items;
    }

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

    public Integer getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(Integer appointment_id) {
        this.appointment_id = appointment_id;
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

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}