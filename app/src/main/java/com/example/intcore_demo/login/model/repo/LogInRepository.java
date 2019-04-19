package com.example.intcore_demo.login.model.repo;

import com.example.intcore_demo.helper.core.AppExecutors;
import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.helper.livedata.NetworkOnlyResource;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.login.model.SignInBody;
import com.example.intcore_demo.login.model.SignUpBody;
import com.example.intcore_demo.login.model.remote.LogInService;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


@Singleton
public class LogInRepository {


    private LogInService logInService;
    private AppExecutors appExecutors;

    @Inject
    public LogInRepository(LogInService logInService) {

        this.logInService = logInService;
        this.appExecutors = new AppExecutors();
    }

    public LiveData<Resource<JsonElement>> getSignUp(SignUpBody body) {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return logInService.signUp(body);
            }
        }.asLiveData();
    }

    public LiveData<Resource<JsonElement>> getSignIn(SignInBody body) {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return logInService.signIn(body);
            }
        }.asLiveData();
    }
}
