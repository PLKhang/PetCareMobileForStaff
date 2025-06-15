package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.notification.NotificationApiRequest;
import com.petcare.staff.data.model.api.notification.NotificationApiResponse;
import com.petcare.staff.data.model.api.notification.NotificationResponse;
import com.petcare.staff.data.model.api.record.IdResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NotificationApi {
    @GET("api/notifications")
    Call<List<NotificationResponse>> getNotifications();

    @POST("/fcm-token")
    Call<NotificationApiResponse> callNotificationApi(@Body NotificationApiRequest request);
}
