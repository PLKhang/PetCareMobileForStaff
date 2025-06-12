package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.api.payment.PaymentMethod;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.repository.PaymentRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

public class ConfirmPaymentViewModel extends AndroidViewModel {
    private final PaymentRepository repository;
    private MutableLiveData<Bill> paymentLiveData = new MutableLiveData<>();
    private boolean isCreated = false;

    public ConfirmPaymentViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PaymentRepository(application.getApplicationContext());
    }

    public LiveData<Bill> getPayment() {
        return paymentLiveData;
    }

    public void updatePaymentMethod(PaymentMethod method, RepositoryCallback callback) {
        Bill bill = paymentLiveData.getValue();
        repository.updatePaymentMethod(bill.getId(), method, callback);
    }

    public void createPayment(Order order, Appointment appointment, float total) {
        String orderId = order != null ? order.getId() : "0";
        String appointmentId = appointment != null ? appointment.getId() : "0";
        PaymentMethod method = PaymentMethod.CASH;
        String description = "nothing";

        Bill bill = new Bill(orderId, appointmentId, total, method, description);
        paymentLiveData.setValue(bill);

        repository.createPayment(bill, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Bill temp = paymentLiveData.getValue();
                temp.setId(message);
                temp.setPaymentMethod(bill.getPaymentMethod());
                paymentLiveData.setValue(temp);
                loadPayment();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void createPaymentUrl() {
        Bill bill = paymentLiveData.getValue();
        repository.createPaymentUrl(bill, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                bill.setPaymentUrl(message);
                paymentLiveData.setValue(bill);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    public void loadPayment() {
        Bill holder = paymentLiveData.getValue();
        LiveData<Bill> liveData = repository.getPaymentById(holder.getId());



        Observer<Bill> observer = new Observer<Bill>() {
            @Override
            public void onChanged(Bill bill) {
                bill.setId(holder.getId()); //
                bill.setPaymentMethod(holder.getPaymentMethod());
                bill.setPaymentUrl(holder.getPaymentUrl());
                paymentLiveData.setValue(bill);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }


    public boolean getIsCreated() {
        return isCreated;
    }

    public void setIsCreated(boolean created) {
        isCreated = created;
    }

    public void updatePaymentStatus(String id, RepositoryCallback repositoryCallback) {
        repository.updatePaymentStatus(id, repositoryCallback);
    }

    public void setPayment(Bill check) {
        paymentLiveData.setValue(check);
    }
}
