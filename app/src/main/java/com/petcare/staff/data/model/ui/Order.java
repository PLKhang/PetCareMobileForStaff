package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.order.OrderStatus;
import com.petcare.staff.utils.DateTime;

import java.util.List;

public class Order {
    private String id;
    private String customer_id;
    private String branch_id;
    private float total_price;
    private DateTime created_at;
    private DateTime updated_at;
    private List<Product> products;
    private Bill bill;
    private String appointment_id;
    private OrderStatus status;

    public Order(String id, String customer_id, String branch_id, String appontment_id, float total_price,
                 DateTime created_at, DateTime updated_at, List<Product> products) {
        this.id = id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appontment_id;
        this.total_price = total_price;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.products = products;
    }

    public Order(String id, String customer_id, String branch_id, float total_price, DateTime created_at,
                 DateTime updated_at, List<Product> products, String appointment_id, OrderStatus status) {
        this.id = id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.total_price = total_price;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.products = products;
        this.appointment_id = appointment_id;
        this.status = status;
    }

    public Order(String id, String customer_id, String branch_id, String appointment_id,
                 OrderStatus status, DateTime created_at, List<Product> products) {
        this.id = id;
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.status = status;
        this.created_at = created_at;
        this.products = products;
    }

    public Order(String customer_id, String branch_id, String appointment_id, List<Product> products)
    {
        this.customer_id = customer_id;
        this.branch_id = branch_id;
        this.appointment_id = appointment_id;
        this.products = products;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public DateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(DateTime updated_at) {
        this.updated_at = updated_at;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
