package com.example.intcore_demo;

import com.example.intcore_demo.profile.model.User;
import com.example.intcore_demo.profile.model.local.ProfileDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProfileDao profileDao();


}
