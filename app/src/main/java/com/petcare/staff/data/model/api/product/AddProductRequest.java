package com.petcare.staff.data.model.api.product;

public class AddProductRequest {
    private String name;
    private String description;
    private float price;

    public AddProductRequest(String name, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
