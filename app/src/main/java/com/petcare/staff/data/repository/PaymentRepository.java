package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.payment.CreatePaymentRequest;
import com.petcare.staff.data.model.api.payment.CreatePaymentResponse;
import com.petcare.staff.data.model.api.payment.CreatePaymentUrlRequest;
import com.petcare.staff.data.model.api.payment.CreatePaymentUrlResponse;
import com.petcare.staff.data.model.api.payment.PaymentMethod;
import com.petcare.staff.data.model.api.payment.PaymentResponse;
import com.petcare.staff.data.model.api.payment.PaymentStatus;
import com.petcare.staff.data.model.api.payment.UpdatePaymentMethodRequest;
import com.petcare.staff.data.model.api.payment.UpdatePaymentMethodResponse;
import com.petcare.staff.data.model.api.payment.UpdatePaymentStatusRequest;
import com.petcare.staff.data.model.api.payment.UpdatePaymentStatusResponse;
import com.petcare.staff.data.model.mapper.PaymentMapper;
import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Payment;
import com.petcare.staff.data.remote.PaymentApi;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {
    private final PaymentApi apiPayment;

    public PaymentRepository(Context context) {
        apiPayment = ApiClient.getPaymentApi(context);
    }

    public LiveData<List<Bill>> getBillListByOrders(List<Order> orderList) {
        return new MutableLiveData<>();
    }

    public void updatePaymentStatus(String paymentId, RepositoryCallback callback) {
        UpdatePaymentStatusRequest request = PaymentMapper.toUpdatePaymentStatusRequest(paymentId, PaymentStatus.COMPLETED);
        apiPayment.updatePaymentStatus(request).enqueue(new Callback<UpdatePaymentStatusResponse>() {
            @Override
            public void onResponse(Call<UpdatePaymentStatusResponse> call, Response<UpdatePaymentStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getStatus());
                } else {
                    callback.onError(new Exception("Update status to COMPLETED failed, code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<UpdatePaymentStatusResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void updatePaymentMethod(String paymentId, PaymentMethod method, RepositoryCallback callback) {
        UpdatePaymentMethodRequest request = PaymentMapper.toUpdatePaymentMethodRequest(paymentId, method);
        apiPayment.updatePaymentMethod(request).enqueue(new Callback<UpdatePaymentMethodResponse>() {
            @Override
            public void onResponse(Call<UpdatePaymentMethodResponse> call, Response<UpdatePaymentMethodResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getStatus());
                } else {
                    callback.onError(new Exception("Updated payment method failed, code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<UpdatePaymentMethodResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void createPayment(Bill payment, RepositoryCallback callback) {
        CreatePaymentRequest request = PaymentMapper.toCreatePaymentRequest(payment);
        apiPayment.createPayment(request).enqueue(new Callback<CreatePaymentResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentResponse> call, Response<CreatePaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(String.valueOf(response.body().getPayment_id()));
                } else {
                    callback.onError(new Exception("Can not createPayment, code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreatePaymentResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void createPaymentUrl(Bill payment, RepositoryCallback callback) {
        CreatePaymentUrlRequest request = PaymentMapper.toCreatePaymentUrlRequest(payment);
        apiPayment.createPaymentUrl(request).enqueue(new Callback<CreatePaymentUrlResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentUrlResponse> call, Response<CreatePaymentUrlResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getPayment_link_id());
                } else {
                    callback.onError(new Exception("Can not create URL, code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreatePaymentUrlResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public LiveData<Bill> getPaymentById(String paymentId) {
        MutableLiveData<Bill> liveData = new MutableLiveData<>();
        apiPayment.getPaymentInfo(Integer.parseInt(paymentId)).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bill bill = PaymentMapper.toBill(response.body());
                    Log.d("API_DEBUG", "Payment status: " + bill.getStatus());
                    liveData.setValue(bill);
                } else {
                    Log.d("API_DEBUG", "Payment null body");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.d("API_DEBUG", "Failure to get payment info: " + t.getMessage(), t);
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
