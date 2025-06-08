package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.user.CustomerResponse;
import com.petcare.staff.data.model.mapper.UserMapper;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.remote.UserApi;
import com.petcare.staff.utils.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {
    private final UserApi apiCustomer;

    public CustomerRepository(Context context) {
        apiCustomer = ApiClient.getUserApi(context);
    }

    public LiveData<List<Customer>> getAllCustomer() {
        MutableLiveData<List<Customer>> customerLiveData = new MutableLiveData<>();
        apiCustomer.getAllCustomer().enqueue(new Callback<List<CustomerResponse>>() {
            @Override
            public void onResponse(Call<List<CustomerResponse>> call, Response<List<CustomerResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Customer> customerList = UserMapper.toListCustomer(response.body());
                    Log.d("API_DEBUG", "Customer: " + customerList.size());
                    customerLiveData.setValue(customerList);
                } else {
                    Log.d("API_DEBUG", "No body");
                    customerLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<CustomerResponse>> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get all customer");
                customerLiveData.setValue(null);
            }
        });
        return customerLiveData;
    }
}
