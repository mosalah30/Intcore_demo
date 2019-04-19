package com.example.intcore_demo.profile.model.remote;

import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.profile.model.ProfileResponse;

import androidx.lifecycle.LiveData;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ProfileService {
    @GET("get-profile?api_token={api_token}")
    LiveData<ApiResponse<ProfileResponse>> getProfileResponse(@Path("api_token") String token);

    @PATCH("update-profile?api_token={api_token}&name={name}&email={email}&image=uploads/{image}")
//.png
    LiveData<ApiResponse<ProfileResponse>> updateProfile(@Path("api_token") String token, @Path("name") String name,
                                                         @Path("email") String email, @Path("image") String image);
}
