package com.example.intcore_demo.profile.model.remote;

import androidx.lifecycle.LiveData;

import com.example.intcore_demo.Constants;
import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.profile.model.ProfileResponse;
import com.google.gson.JsonElement;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfileService {
    @GET("get-profile?")
    LiveData<ApiResponse<ProfileResponse>> getProfileResponse(@Query("api_token") String token);

    @PATCH("update-password?")
    LiveData<ApiResponse<JsonElement>> updatePassword(@Query("api_token") String token, @Query("new_password") String newPassword, @Query("old_password") String oldPassword);

    @PATCH("update-phone?")
    LiveData<ApiResponse<JsonElement>> updatePhone(@Query("api_token") String token, @Query("phone") String phone, @Query("temp_phone_code") String phoneCode);

    @POST("request-update-phone")
    LiveData<ApiResponse<JsonElement>> requestUpdatePhone(@Query(Constants.Token_ID_KEY) String token, @Query("phone") String phoneNumber);

    @PATCH("update-profile?")
    LiveData<ApiResponse<JsonElement>> updateProfile(@Query("api_token") String token, @Query("name") String name,

                                                     @Query("email") String email, @Query("image") String image);


}
