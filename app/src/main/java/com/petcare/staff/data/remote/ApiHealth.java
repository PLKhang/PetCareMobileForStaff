package com.petcare.staff.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiHealth {
    @GET("api/v1/hello-world")
    Call<String> checkServerHealth();
}