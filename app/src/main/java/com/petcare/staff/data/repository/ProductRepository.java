package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.product.AllProductResponse;
import com.petcare.staff.data.model.api.product.BranchResponse;
import com.petcare.staff.data.model.api.product.ProductResponse;
import com.petcare.staff.data.model.mapper.ProductMapper;
import com.petcare.staff.data.model.ui.Branch;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.remote.ProductApi;
import com.petcare.staff.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ProductApi apiProduct;

    public ProductRepository(Context context) {
        apiProduct = ApiClient.getProductApi(context);
    }

    public LiveData<List<Product>> getAllProducts(String branchId) {
        MutableLiveData<List<Product>> productLiveData = new MutableLiveData<>();
        apiProduct.getAllProducts(Integer.parseInt(branchId)).enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = ProductMapper.toProductList(response.body());
                    Log.d("API_DEBUG", "Products size: " + productList.size());
                    for (Product p: productList) {
                        Log.d("API_DEBUG", "Product: " + p);
                    }
                    productLiveData.setValue(productList);
                } else {

                    Log.d("API_DEBUG", "No body");
                    productLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get all products");
                productLiveData.setValue(null);
            }
        });
        return productLiveData;
    }

    public LiveData<Branch> getBranchInfo(String branchId) {
        MutableLiveData<Branch> liveData = new MutableLiveData<>();
        apiProduct.getBranchById(Integer.parseInt(branchId)).enqueue(new Callback<BranchResponse>() {
            @Override
            public void onResponse(Call<BranchResponse> call, Response<BranchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Branch branch = ProductMapper.toBranch(response.body());
                    Log.d("API_DEBUG", "Branch name: " + branch.getName());
                    liveData.setValue(branch);
                } else {
                    Log.d("API_DEBUG", "Null body");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BranchResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get branch info");
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> productLiveData = new MutableLiveData<>();
        apiProduct.getAllBranchesProducts().enqueue(new Callback<List<AllProductResponse>>() {
            @Override
            public void onResponse(Call<List<AllProductResponse>> call, Response<List<AllProductResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = ProductMapper.toProductListFromAllProduct(response.body());
                    Log.d("API_DEBUG", "Products size: " + productList.size());
                    for (Product product: productList) {
                        Log.d("API_DEBUG", "ProductId: " + product.getId());
                    }
                    productLiveData.setValue(productList);
                } else {

                    Log.d("API_DEBUG", "No body, code: " + response.code());
                    productLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<AllProductResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get all branch products");
                productLiveData.setValue(null);
            }
        });
        return productLiveData;
    }
}
