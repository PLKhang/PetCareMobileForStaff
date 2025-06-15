package com.petcare.staff.utils;

import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "New token: " + token);

        // TODO: Gửi token này lên server backend của bạn
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "Thông báo";
        String body = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "";

        // Bạn có thể lấy dữ liệu custom nếu gửi từ server dạng "data"
        if (remoteMessage.getData().size() > 0) {
            String id = remoteMessage.getData().get("id");
            String type = remoteMessage.getData().get("type");
            // Xử lý tùy biến
        }
        Log.d("Notification", "title: " + title + "body: " + body);
        NotificationUtils.showNotification(this, title, body);
        // Đồng bộ với Room nếu muốn
    }

//    private void showNotification(String title, String message) {
//        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
//        String channelId = "petcare_channel";
//
//        // Android 8+ cần tạo NotificationChannel
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    channelId, "PetCare Notification", NotificationManager.IMPORTANCE_HIGH);
//            manager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        manager.notify((int) System.currentTimeMillis(), builder.build());
//    }
}