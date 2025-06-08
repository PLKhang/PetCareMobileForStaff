package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.order.CreateOrderRequest;
import com.petcare.staff.data.model.api.order.CreateOrderResponse;
import com.petcare.staff.data.model.api.order.OrderItemResponse;
import com.petcare.staff.data.model.api.order.OrderResponse;
import com.petcare.staff.data.model.api.order.UpdateOrderStatusRequest;
import com.petcare.staff.data.model.api.order.UpdateOrderStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderApi {

    @POST("api/v1/orders")
    Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest request);

    @GET("api/v1/orders/{order_id}")
    Call<OrderResponse> getOrder(@Path("order_id") int orderId);

    @GET("api/v1/orders/customer/{customer_id}")
    Call<List<OrderResponse>> listOrderByCustomer(@Path("customer_id") int customer_id);

    @GET("api/v1/orders/appointment/{appointment_id}")
    Call<OrderResponse> getOrderByAppointmentId(@Path("appointment_id") int appointmentId);

    @PUT("api/v1/orders/update-status")
    Call<UpdateOrderStatusResponse> updateOrderStatus(@Body UpdateOrderStatusRequest request);

    @GET("api/v1/orders/{order_id}/items")
    Call<List<OrderItemResponse>> getOrderItems(@Path("order_id") int orderId);
}
