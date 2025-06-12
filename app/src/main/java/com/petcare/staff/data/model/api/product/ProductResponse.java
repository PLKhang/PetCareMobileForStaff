package com.petcare.staff.data.model.api.product;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("product_id")
    private int id;
    private String name;
    private float price;
    private String description;
    private String imgurl;
    private ProductType product_type;
    private boolean is_attachable;
    private int available_quantity;

    public ProductResponse(int id, String name, float price, String description,
                           String imgurl, ProductType product_type, boolean is_attachable, int available_quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgurl = imgurl;
        this.product_type = product_type;
        this.is_attachable = is_attachable;
        this.available_quantity = available_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgurl;
    }

    public void setImgUrl(String imgurl) {
        this.imgurl = imgurl;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public boolean isIs_attachable() {
        return is_attachable;
    }

    public void setIs_attachable(boolean is_attachable) {
        this.is_attachable = is_attachable;
    }

    public int getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(int available_quantity) {
        this.available_quantity = available_quantity;
    }
}
