package com.petcare.staff.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.remote.PaymentApi;
import com.petcare.staff.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final PaymentApi apiPayment;
    public PaymentRepository(Context context) {
        apiPayment = ApiClient.getPaymentApi(context);
    }

    public LiveData<List<Bill>> getBillListByOrders(List<Order> orderList) {
        return new MutableLiveData<>();
    }
}
