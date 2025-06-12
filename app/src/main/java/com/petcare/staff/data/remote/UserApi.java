package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.user.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    // Auth api
    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/v1/auth/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/v1/auth/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);

    // User api -> employee
    @GET("api/v1/users/me")
    Call<UserResponse> getCurrentUser();

    @GET("api/v1/users/{id}")
    Call<UserByIdResponse> getUserInfoById(@Path("id") int id);

    @GET("api/v1/user/email")
    Call<UserResponse> getUserInfoByEmail(@Query("email") String email);
    @PUT("api/v1/users/me")
    Call<ChangeInfoResponse> changeInfo(@Body ChangeInfoRequest request);

    @PUT("api/v1/users/me/password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

    @GET("api/v1/customers")
    Call<List<CustomerResponse>> getAllCustomer();

    @POST("api/v1/users")
    Call<CreateCustomerResponse> createNewCustomer(@Body CreateCustomerRequest request);
}
