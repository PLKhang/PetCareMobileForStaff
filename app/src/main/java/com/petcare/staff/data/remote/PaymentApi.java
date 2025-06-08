package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.payment.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PaymentApi {

    @POST("api/v1/payments")
    Call<CreatePaymentResponse> createPayment(@Body CreatePaymentRequest request);

    @GET("api/v1/payments/{payment_id}")
    Call<PaymentResponse> getPaymentInfo(@Path("payment_id") int paymentId);

    @POST("api/v1/payments/url")
    Call<CreatePaymentUrlResponse> createPaymentUrl(@Body CreatePaymentUrlRequest request);

    @POST("api/v1/payments/cancel")
    Call<CancelPaymentResponse> cancelPaymentLink(@Body CancelPaymentRequest request);

    @PUT("api/v1/payments/update-status")
    Call<UpdatePaymentStatusResponse> updatePaymentStatus(@Body UpdatePaymentStatusRequest request);

    @PUT("api/v1/payments/update-method")
    Call<UpdatePaymentMethodResponse> updatePaymentMethod(@Body UpdatePaymentMethodRequest request);

    @PUT("api/v1/payments/update-amount")
    Call<UpdatePaymentAmountResponse> updatePaymentAmount(@Body UpdatePaymentAmountRequest request);
}
