package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.product.AddProductRequest;
import com.petcare.staff.data.model.api.product.AddProductResponse;
import com.petcare.staff.data.model.api.product.BranchResponse;
import com.petcare.staff.data.model.api.product.InventoryResponse;
import com.petcare.staff.data.model.api.product.ProductResponse;
import com.petcare.staff.data.model.api.product.UpdateInventoryRequest;
import com.petcare.staff.data.model.api.product.UpdateInventoryResponse;
import com.petcare.staff.data.model.api.product.UpdateProductRequest;
import com.petcare.staff.data.model.api.product.UpdateProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductApi {
    // Tất cả sản phẩm & sản phẩm có thể đính kèm
    @GET("api/v1/products")
    Call<List<ProductResponse>> getAllProducts();

    @GET("api/v1/products/is_attachable")
    Call<List<ProductResponse>> getAllAttachableProducts();

    // Thực phẩm
    @GET("api/v1/products/foods")
    Call<List<ProductResponse>> getAllFoods();

    @GET("api/v1/products/foods/{id}")
    Call<ProductResponse> getFoodById(@Path("id") int id);

    @POST("api/v1/products/foods")
    Call<AddProductResponse> createFood(@Body AddProductRequest request);

    @PUT("api/v1/products/foods")
    Call<UpdateProductResponse> updateFood(@Body UpdateProductRequest request);

    @DELETE("api/v1/products/foods/{id}")
    Call<Void> deleteFood(@Path("id") int id);

    // Phụ kiện
    @GET("api/v1/products/accessories")
    Call<List<ProductResponse>> getAllAccessories();

    @GET("api/v1/products/accessories/{id}")
    Call<ProductResponse> getAccessoryById(@Path("id") int id);

    @POST("api/v1/products/accessories")
    Call<AddProductResponse> createAccessory(@Body AddProductRequest request);

    @PUT("api/v1/products/accessories")
    Call<UpdateProductResponse> updateAccessory(@Body UpdateProductRequest request);

    @DELETE("api/v1/products/accessories/{id}")
    Call<Void> deleteAccessory(@Path("id") int id);

    // Thuốc
    @GET("api/v1/products/medicines")
    Call<List<ProductResponse>> getAllMedicines();

    @GET("api/v1/products/medicines/{id}")
    Call<ProductResponse> getMedicineById(@Path("id") int id);

    @POST("api/v1/products/medicines")
    Call<AddProductResponse> createMedicine(@Body AddProductRequest request);

    @PUT("api/v1/products/medicines")
    Call<UpdateProductResponse> updateMedicine(@Body UpdateProductRequest request);

    @DELETE("api/v1/products/medicines/{id}")
    Call<Void> deleteMedicine(@Path("id") int id);

    // Chi nhánh
    @GET("api/v1/branches")
    Call<List<BranchResponse>> getAllBranches();

    @GET("api/v1/branches/{id}")
    Call<BranchResponse> getBranchById(@Path("id") int id);

    // Tồn kho theo chi nhánh
    @GET("api/v1/branches/{branch_id}/inventory")
    Call<InventoryResponse> getBranchInventory(@Path("branch_id") int branchId);

    @PUT("api/v1/branches/inventory")
    Call<UpdateInventoryResponse> updateBranchInventory(@Body UpdateInventoryRequest request);
}
