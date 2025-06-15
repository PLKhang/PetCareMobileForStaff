package com.petcare.staff.ui.home.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.local.AppDatabase;
import com.petcare.staff.data.local.entity.NotificationEntity;
import com.petcare.staff.data.model.api.notification.NotificationApiRequest;
import com.petcare.staff.data.model.api.notification.NotificationResponse;
import com.petcare.staff.data.repository.NotificationRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository repository;
//    private final LiveData<List<NotificationResponse>> notifications;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application.getApplicationContext());
//        notifications = repository.getNotifications();
    }
//    public LiveData<List<NotificationResponse>> getNotifications() {
//        return notifications;
//    }

    public void sendNotificationRequest(NotificationApiRequest request, RepositoryCallback callback)
    {
        repository.callNotificationApi(request, callback);
    }
}
