package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.product.ProductResponse;
import com.petcare.staff.data.model.api.product.ProductType;

public class Product {
    private String id;
    private String name;
    private ProductType type;
    private String description;
    private String imgUrl;
    private float price;
    private int quantity;
    private int stock;
    private boolean isAttachable;

    public Product(String id, String name, float price, String description, String imgUrl, ProductType type, boolean isAttachable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.type = type;
        this.isAttachable = isAttachable;
    }

    public Product(String id, ProductType type, int quantity) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
    }

    public Product(String id, ProductType type) {
        this.id = id;
        this.type = type;
    }

    public Product(String id, String name, String description, int stock,
                   String imgUrl, float price, boolean isAttachable, ProductType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.stock = stock;
        this.isAttachable = isAttachable;
        this.type = type;
        this.quantity = 1;
    }

    public Product(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.description = p.getDescription();
        this.imgUrl = p.getImgUrl();
        this.price = p.getPrice();
        this.isAttachable = p.isAttachable();
        this.type = p.getType();
        this.quantity = p.getQuantity();
        this.stock = p.getStock();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAttachable() {
        return isAttachable;
    }

    public void setAttachable(boolean attachable) {
        isAttachable = attachable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + (type != null ? type.toString() : "null") +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", stock=" + stock +
                ", isAttachable=" + isAttachable +
                '}';
    }

    public void copy(Product p) {
        this.id = p.id;
        this.name = p.name;
        this.type = p.type;
        this.description = p.description;
        this.imgUrl = p.imgUrl;
        this.price = p.price;
        this.quantity = p.quantity;
        this.stock = p.stock;
        this.isAttachable = p.isAttachable;
    }

}
