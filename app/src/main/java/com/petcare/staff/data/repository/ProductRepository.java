package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.product.ProductResponse;
import com.petcare.staff.data.model.mapper.ProductMapper;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.remote.ProductApi;
import com.petcare.staff.utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ProductApi apiProduct;

    public ProductRepository(Context context) {
        apiProduct = ApiClient.getProductApi(context);
    }

    public LiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> productLiveData = new MutableLiveData<>();
        apiProduct.getAllProducts().enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    List<Product> productList = ProductMapper.toProductList(response.body());
                    Log.d("API_DEBUG", "Product: " + productList.size());
                    productLiveData.setValue(productList);
                }
                else {

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
}
