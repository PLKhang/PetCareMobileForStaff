package com.petcare.staff.data.model.api.product;

public class InventoryResponse {
    private int branch_id;
    private int product_id;
    private ProductType product_type;
    private int stock_quantity;

    public InventoryResponse(int branch_id, int product_id, ProductType product_type, int stock_quantity) {
        this.branch_id = branch_id;
        this.product_id = product_id;
        this.product_type = product_type;
        this.stock_quantity = stock_quantity;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }
}
