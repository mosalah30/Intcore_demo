package com.example.intcore_demo.profile.model.local;


import com.example.intcore_demo.profile.model.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(User user);

    @Query("SELECT * FROM user WHere id= 0")
    LiveData<User> getProfile();
}
