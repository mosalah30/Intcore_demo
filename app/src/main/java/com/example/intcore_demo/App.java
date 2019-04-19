package com.example.intcore_demo;

import android.content.Context;

import com.example.intcore_demo.di.AppComponent;
import com.example.intcore_demo.di.DaggerAppComponent;
import com.example.intcore_demo.di.module.ApiModule;
import com.example.intcore_demo.di.module.AppModule;
import com.example.intcore_demo.di.module.DaoModule;
import com.example.intcore_demo.di.module.NetModule;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static AppComponent appComponent;
    private static App instance;


    public static Context get() {
        return instance;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder().
                appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .netModule(new NetModule("https://internship-api-v0.7.intcore.net/api/v1/user/auth/"))
                .daoModule(new DaoModule())
                .build();
    }


}
