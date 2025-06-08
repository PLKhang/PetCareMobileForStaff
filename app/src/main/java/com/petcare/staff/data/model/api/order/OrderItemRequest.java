package com.petcare.staff.data.model.api.order;

import com.petcare.staff.data.model.api.product.ProductType;

public class OrderItemRequest {
    private int product_id;
    private ProductType product_type;
    private int quantity;
    private float unit_price;
    private String product_name;

    public OrderItemRequest(int product_id, ProductType product_type, int quantity, float unit_price, String product_name) {
        this.product_id = product_id;
        this.product_type = product_type;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.product_name = product_name;
    }
// getters & setters

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
