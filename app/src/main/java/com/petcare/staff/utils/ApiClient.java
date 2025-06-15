package com.petcare.staff.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petcare.staff.data.remote.NotificationApi;
import com.petcare.staff.data.remote.AppointmentApi;
import com.petcare.staff.data.remote.OrderApi;
import com.petcare.staff.data.remote.PaymentApi;
import com.petcare.staff.data.remote.ProductApi;
import com.petcare.staff.data.remote.RecordApi;
import com.petcare.staff.data.remote.UserApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static Retrofit defaultRetrofit;
    private static Retrofit notificationRetrofit;

    private static OkHttpClient createClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    String latestToken = SharedPrefManager.getToken(context);
                    Log.d("TOKEN_DEBUG", "Using token: " + latestToken);

                    Request.Builder builder = original.newBuilder();
                    if (latestToken != null && !latestToken.isEmpty()) {
                        builder.header("Authorization", "Bearer " + latestToken);
                    }

                    Request request = builder.build();
                    return chain.proceed(request);
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new CaseInsensitiveEnumAdapterFactory())
                .create();
    }

    public static Retrofit getInstance(Context context) {
        if (defaultRetrofit == null) {
            defaultRetrofit = new Retrofit.Builder()
                    .baseUrl("http://26.199.48.182:8080/")
                    .client(createClient(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return defaultRetrofit;
    }

    public static Retrofit getNotificationInstance(Context context) {
        if (notificationRetrofit == null) {
            notificationRetrofit = new Retrofit.Builder()
                    .baseUrl("http://26.199.48.182:8088") //
                    .client(createClient(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return notificationRetrofit;
    }

    // Các API sử dụng base chính
    public static UserApi getUserApi(Context context) {
        return getInstance(context).create(UserApi.class);
    }

    public static AppointmentApi getAppointmentApi(Context context) {
        return getInstance(context).create(AppointmentApi.class);
    }

    public static OrderApi getOrderApi(Context context) {
        return getInstance(context).create(OrderApi.class);
    }

    public static PaymentApi getPaymentApi(Context context) {
        return getInstance(context).create(PaymentApi.class);
    }

    public static ProductApi getProductApi(Context context) {
        return getInstance(context).create(ProductApi.class);
    }

    public static RecordApi getRecordApi(Context context) {
        return getInstance(context).create(RecordApi.class);
    }

    // Notification API riêng
    public static NotificationApi getNotificationApi(Context context) {
        return getNotificationInstance(context).create(NotificationApi.class);
    }
}
