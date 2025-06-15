package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.notification.NotificationResponse;
import com.petcare.staff.data.model.ui.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationMapper {
    public static List<Notification> toNotificationList(List<NotificationResponse> responses) {
        List<Notification> result = new ArrayList<>();
        for (NotificationResponse response:responses)
        {
            result.add(toNotification(response));
        }

        return result;
    }

    private static Notification toNotification(NotificationResponse response) {
        return new Notification();
    }
}
