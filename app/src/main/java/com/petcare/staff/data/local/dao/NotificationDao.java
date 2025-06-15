package com.petcare.staff.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;

import com.petcare.staff.data.local.entity.NotificationEntity;

import java.util.List;

import retrofit2.http.Query;

@Dao
public interface NotificationDao {
//    @Insert
//    void insert(NotificationEntity notification);
//
//    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
//    LiveData<List<NotificationEntity>> getAllNotifications();
}
