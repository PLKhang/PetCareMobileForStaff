package com.petcare.staff.data.model.api.product;

public class UpdateInventoryRequest {
    private int branch_id;
    private int product_id;
    private ProductType product_type;
    private int stock_quantity;

    public UpdateInventoryRequest(int branch_id, int product_id, ProductType product_type, int stock_quantity) {
        this.branch_id = branch_id;
        this.product_id = product_id;
        this.product_type = product_type;
        this.stock_quantity = stock_quantity;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }
}
