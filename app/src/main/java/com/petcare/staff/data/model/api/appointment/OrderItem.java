package com.petcare.staff.data.model.api.appointment;

import com.petcare.staff.data.model.api.product.ProductType;

public class OrderItem {
    private int order_id;
    private int product_id;
    private int quantity;
    private float unit_price;
    private ProductType product_type;

    public OrderItem(int order_id, int product_id, int quantity, float unit_price, ProductType product_type) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.product_type = product_type;
    }
// getters & setters

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }
}
