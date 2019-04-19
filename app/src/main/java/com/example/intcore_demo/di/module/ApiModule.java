package com.example.intcore_demo.di.module;


import com.example.intcore_demo.login.model.remote.LogInService;
import com.example.intcore_demo.profile.model.remote.ProfileService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public LogInService providesNewsService(Retrofit retrofit) {
        return retrofit.create(LogInService.class);
    }

    @Provides
    @Singleton
    public ProfileService providesProfileService(Retrofit retrofit) {
        return retrofit.create(ProfileService.class);
    }
}
