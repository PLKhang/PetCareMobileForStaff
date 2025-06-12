package com.petcare.staff.ui.billing.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Product;

public class SharedOrderViewModel extends ViewModel {
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<String> selectedTime = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<Float> total = new MutableLiveData<Float>();
    private MutableLiveData<Order> orderLiveData = new MutableLiveData<>();

    public LiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    public void setOrderLiveData(Order order) {
        for (Product p: order.getProducts()){
        Log.d("SetOrderLiveData", "Product: " +p.toString());
        }

        this.orderLiveData.setValue(order);
    }


    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String time) {
        selectedTime.setValue(time);
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public void setAddress(String value) {
        address.setValue(value);
    }

    public MutableLiveData<Float> getTotal() {
        return total;
    }

    public void setTotal(float total)
    {
        this.total.setValue(total);
    }

    public void resetOrderLiveData(){
        this.orderLiveData.setValue(null);
    }
}
