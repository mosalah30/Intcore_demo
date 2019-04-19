package com.example.intcore_demo.di.module;


import android.app.Application;

import com.example.intcore_demo.AppDatabase;
import com.example.intcore_demo.profile.model.local.ProfileDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Application app) {
        return Room.databaseBuilder(app,
                AppDatabase.class, "Intcore_demo_localDB.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public ProfileDao provideProfileDao(AppDatabase appDatabase) {
        return appDatabase.profileDao();
    }

}
