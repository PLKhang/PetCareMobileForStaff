package com.petcare.staff.data.local;

import android.content.Context;

import androidx.room.Database;

import com.petcare.staff.data.local.dao.NotificationDao;
import com.petcare.staff.data.local.entity.NotificationEntity;

@Database(entities = {NotificationEntity.class}, version = 1)
public abstract class AppDatabase  {
//    public abstract class AppDatabase extends RoomDatabase {
//    private static volatile AppDatabase instance;
//
//    public abstract NotificationDao notificationDao();
//
//    public static AppDatabase getInstance(Context context) {
//        if (instance == null) {
//            instance = Room.databaseBuilder(context.getApplicationContext(),
//                    AppDatabase.class, "notification_db").build();
//        }
//        return instance;
//    }
}
