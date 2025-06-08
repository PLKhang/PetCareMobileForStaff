package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.product.AddProductRequest;
import com.petcare.staff.data.model.api.product.BranchResponse;
import com.petcare.staff.data.model.api.product.InventoryResponse;
import com.petcare.staff.data.model.api.product.ProductResponse;
import com.petcare.staff.data.model.api.product.UpdateInventoryRequest;
import com.petcare.staff.data.model.api.product.UpdateProductRequest;
import com.petcare.staff.data.model.ui.Branch;
import com.petcare.staff.data.model.ui.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static Branch toBranch(BranchResponse response) {
        return new Branch(
                String.valueOf(response.getId()),
                response.getName(),
                response.getLocation()
        );
    }

    public static List<Branch> toBranchList(List<BranchResponse> responses) {
        List<Branch> branches = new ArrayList<>();
        for (BranchResponse response: responses) {
            branches.add(toBranch(response));
        }

        return branches;
    }

    public static Product toProductStock(InventoryResponse response) {
        Product productStock = new Product(
                String.valueOf(response.getProduct_id()),
                response.getProduct_type()
        );

        productStock.setStock(response.getStock_quantity());

        return productStock;
    }

    public static Product toProduct(ProductResponse response) {
        return new Product(
                String.valueOf(response.getId()),
                response.getName(),
                response.getDescription(),
                response.getImgUrl(),
                response.getPrice(),
                response.isAttachable(),
                response.getProductType()
        );
    }

    public static List<Product> toProductList(List<ProductResponse> responses) {
        List<Product> products = new ArrayList<>();
        for (ProductResponse response: responses) {
            products.add(toProduct(response));
        }
        return products;
    }

    public static AddProductRequest toAddProductRequest(Product product) {
        return new AddProductRequest(
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static UpdateProductRequest toUpdateProductRequest(Product product) {
        return new UpdateProductRequest(
                Integer.parseInt(product.getId()),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static UpdateInventoryRequest toUpdateInventoryRequest(int branch_id, Product product){
        return new UpdateInventoryRequest(
                branch_id,
                Integer.parseInt(product.getId()),
                product.getType(),
                product.getStock()
        );
    }
}

