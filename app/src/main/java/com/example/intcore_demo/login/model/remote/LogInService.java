package com.example.intcore_demo.login.model.remote;

import androidx.lifecycle.LiveData;

import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.login.model.SignInBody;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogInService {

    @POST("signup")
    LiveData<ApiResponse<JsonElement>> signUp(@Body JsonObject body);

    @POST("signin")
    LiveData<ApiResponse<JsonElement>> signIn(@Body SignInBody body);


}
