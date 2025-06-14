package com.petcare.staff.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private static Retrofit retrofit;

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            String token = SharedPrefManager.getToken(context);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // Lấy token mỗi lần thực hiện request
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

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new CaseInsensitiveEnumAdapterFactory())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") //10.0.2.2 // http://26.199.48.182:8080/  http://192.168.1.8:8080/ http://127.0.0.1:8080/
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create()) //health check
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

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
}
