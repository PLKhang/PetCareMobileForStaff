package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.notification.NotificationApiRequest;
import com.petcare.staff.data.model.api.notification.NotificationApiResponse;
import com.petcare.staff.data.model.mapper.NotificationMapper;
import com.petcare.staff.data.model.ui.Notification;
import com.petcare.staff.data.remote.NotificationApi;
import com.petcare.staff.data.model.api.notification.NotificationResponse;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {
    private final NotificationApi notificationApi;
    private final MutableLiveData<List<NotificationResponse>> notifications = new MutableLiveData<>();

    public NotificationRepository(Context context) {
        this.notificationApi = ApiClient.getNotificationApi(context);
    }

    public LiveData<List<NotificationResponse>> getNotifications() {
        MutableLiveData <List<Notification>> liveData = new MutableLiveData<>();
        notificationApi.getNotifications().enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notificationList = NotificationMapper.toNotificationList(response.body());
                    Log.e("NotificationRepo", "Notifications: " + notificationList.size());
                    liveData.setValue(notificationList);
                } else {
                    Log.e("NotificationRepo", "API call fail, code: " + response.code());
                    liveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                liveData.setValue(new ArrayList<>());
                Log.e("NotificationRepo", "API call failed: " + t.getMessage());
            }
        });

        return notifications;
    }

    public void callNotificationApi(NotificationApiRequest request, RepositoryCallback callback) {
        notificationApi.callNotificationApi(request).enqueue(new Callback<NotificationApiResponse>() {
            @Override
            public void onResponse(Call<NotificationApiResponse> call, Response<NotificationApiResponse> response) {
                if (response.isSuccessful() &&response.body() != null)
                {
                    callback.onSuccess("Notification: " + response.body().getMessage());
                }
                else {
                    callback.onError(new Exception("Fail to send notification request"));
                }
            }

            @Override
            public void onFailure(Call<NotificationApiResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
