package com.petcare.staff.data.model.api.product;

public class AllProductResponse {
    private int id;
    private String name;
    private float price;
    private String description;
    private String imgUrl;
    private ProductType productType;
    private Boolean isAttachable;

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
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Boolean getAttachable() {
        return isAttachable;
    }

    public void setAttachable(Boolean attachable) {
        isAttachable = attachable;
    }
}
