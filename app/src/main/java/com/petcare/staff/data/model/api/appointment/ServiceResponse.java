package com.petcare.staff.data.model.api.appointment;

public class ServiceResponse {
    private int serviceId;
    private String name;
    private String description;
    private float price;
    private String imgUrl;

    public int getId() {
        return serviceId;
    }

    public void setId(int id) {
        this.serviceId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage_url() {
        return imgUrl;
    }

    public void setImage_url(String image_url) {
        this.imgUrl = image_url;
    }
}
