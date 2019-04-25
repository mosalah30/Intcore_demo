package com.example.intcore_demo.profile.model.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.intcore_demo.profile.model.User;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfile(User user);

    @Query("SELECT * FROM user")
    LiveData<User> getProfile();

    @Query("DELETE   FROM user")
    void deleteProfile();
}
