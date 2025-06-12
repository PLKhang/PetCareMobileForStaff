package com.petcare.staff.data.model.ui;

public class Service {
    private String id;
    private String name;
    private String description;
    private float price;
    private int quantity;
    private String imageUrl;

    public Service(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Service(String id, String name, String description, float price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = 1;
    }

    public Service(Service s) {
        this.id = s.getId();
        this.name = s.getName();
        this.description = s.getDescription();
        this.price = s.getPrice();
        this.imageUrl = s.getImageUrl();
        this.quantity = s.getQuantity();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void copy(Service s) {
        this.id = s.id;
        this.name = s.name;
        this.description = s.description;
        this.price = s.price;
        this.quantity = s.quantity;
        this.imageUrl = s.imageUrl;
    }
}
