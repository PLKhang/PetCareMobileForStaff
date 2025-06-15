package com.petcare.staff.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.petcare.staff.data.model.api.notification.NotificationApiRequest;
import com.petcare.staff.data.model.api.notification.NotificationApiResponse;
import com.petcare.staff.data.remote.NotificationApi;
import com.petcare.staff.data.model.api.notification.NotificationResponse;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.utils.ApiClient;

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
        notificationApi.getNotifications().enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notifications.postValue(response.body());
                } else {
                    notifications.postValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                notifications.postValue(Collections.emptyList());
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
