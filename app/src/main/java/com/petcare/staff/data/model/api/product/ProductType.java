package com.petcare.staff.data.model.api.product;

public enum ProductType {
    FOOD("Thức ăn"),
    ACCESSORY("Phụ kiện"),
    MEDICINE("Thuốc");

    private final String label;

    ProductType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
