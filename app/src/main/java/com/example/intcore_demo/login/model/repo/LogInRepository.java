package com.example.intcore_demo.login.model.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.intcore_demo.helper.core.AppExecutors;
import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.helper.livedata.NetworkOnlyResource;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.login.model.SignInBody;
import com.example.intcore_demo.login.model.remote.LogInService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class LogInRepository {


    private LogInService logInService;
    private AppExecutors appExecutors;

    @Inject
    public LogInRepository(LogInService logInService) {

        this.logInService = logInService;
        this.appExecutors = new AppExecutors();
    }

    public LiveData<Resource<JsonElement>> getSignUp(String email, String password, String name, String phone) {
        JsonObject object = new JsonObject();

        object.addProperty("name", name);
        object.addProperty("phone", phone);
        object.addProperty("password", password);
        object.addProperty("email", email);


        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return logInService.signUp(object);
            }
        }.asLiveData();
    }

    public LiveData<Resource<JsonElement>> getSignIn(String nameOrEmail, String password) {
        SignInBody body = new SignInBody();
        body.setName(nameOrEmail);
        body.setPassword(password);
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
