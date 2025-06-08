package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.order.CreateOrderRequest;
import com.petcare.staff.data.model.api.order.CreateOrderResponse;
import com.petcare.staff.data.model.api.order.OrderResponse;
import com.petcare.staff.data.model.mapper.OrderMapper;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.remote.OrderApi;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final OrderApi apiOrder;
    public OrderRepository(Context context) {
        apiOrder = ApiClient.getOrderApi(context);
    }

    public LiveData<List<Order>> getOrdersByCustomerId(String id) {
        MutableLiveData<List<Order>> orderLiveData = new MutableLiveData<>();
        apiOrder.listOrderByCustomer(Integer.parseInt(id)).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = OrderMapper.toOrdersList(response.body());
                    Log.d("API_DEBUG", "Order: " + orderList.size());
                    orderLiveData.setValue(orderList);
                } else {
                    Log.d("API_DEBUG", "No body");
                    orderLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get order by customer id");
                orderLiveData.setValue(null);
            }
        });

        return orderLiveData;
    }

    public void createOrder(Order order, RepositoryCallback repositoryCallback) {
        CreateOrderRequest request = OrderMapper.toCreateOrderRequest(order);
        apiOrder.createOrder(request).enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    String createdId = String.valueOf(response.body().getOrder_id());
                    repositoryCallback.onSuccess(createdId);
                }
                else {
                    repositoryCallback.onError(new Exception("Failed to create order. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                repositoryCallback.onError(t);
            }
        });
    }
}
