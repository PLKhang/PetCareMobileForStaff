package com.petcare.staff.data.model.api.product;

public class ProductResponse {
    private int id;
    private String name;
    private float price;
    private String description;
    private String imgUrl;
    private ProductType productType;
    private boolean isAttachable;

    public ProductResponse(int id, String name, float price, String description,
                           String imgUrl, ProductType productType, boolean isAttachable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.productType = productType;
        this.isAttachable = isAttachable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setAttachable(boolean attachable) {
        isAttachable = attachable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ProductType getProductType() {
        return productType;
    }

    public boolean isAttachable() {
        return isAttachable;
    }
}
