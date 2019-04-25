package com.example.intcore_demo.profile.model.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.intcore_demo.helper.core.AppExecutors;
import com.example.intcore_demo.helper.core.SharedPreferencesHelper;
import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.helper.livedata.NetworkBoundResource;
import com.example.intcore_demo.helper.livedata.NetworkOnlyResource;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.utilities.ShouldFetch;
import com.example.intcore_demo.profile.model.ProfileResponse;
import com.example.intcore_demo.profile.model.User;
import com.example.intcore_demo.profile.model.local.ProfileDao;
import com.example.intcore_demo.profile.model.remote.ProfileService;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProfileRepository {
    private ProfileDao profileDao;
    private ProfileService profileService;
    private AppExecutors appExecutors;

    @Inject
    public ProfileRepository(ProfileDao profileDao, ProfileService profileService) {
        this.profileDao = profileDao;
        this.profileService = profileService;
        this.appExecutors = new AppExecutors();
    }

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    public LiveData<Resource<User>> getProfile() {

        return new NetworkBoundResource<User, ProfileResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull ProfileResponse item) {
                if (item.getUser() != null)
                    profileDao.insertProfile(item.getUser());
            }

            @Override
            protected void onFetchFailed() {
                loadFromDb();
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return ShouldFetch.networkRecommended();
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return profileDao.getProfile();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ProfileResponse>> createCall() {
                return profileService.getProfileResponse(sharedPreferencesHelper.getUserToken());
            }
        }.asLiveData();
    }

    public LiveData<Resource<JsonElement>> updateProfile(String name,
                                                         String email, String image) {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return profileService.updateProfile(sharedPreferencesHelper.getUserToken(), name, email, image);
            }
        }.asLiveData();
    }

    public LiveData<Resource<JsonElement>> updatePassword(String newPassword,
                                                          String oldPassword) {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return profileService.updatePassword(sharedPreferencesHelper.getUserToken(), newPassword, oldPassword);
            }
        }.asLiveData();
    }

    public void Logout() {
        appExecutors.diskIO().execute(() -> profileDao.deleteProfile());
        sharedPreferencesHelper.setUserLoggedIn(false);
    }

    public LiveData<Resource<JsonElement>> updateNumberPhone(String phone, String codePhone) {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return profileService.updatePhone(sharedPreferencesHelper.getUserToken(), phone, codePhone);
            }
        }.asLiveData();
    }

    public LiveData<Resource<JsonElement>> requestUpdateNumberPhone() {
        return new NetworkOnlyResource<JsonElement, JsonElement>(appExecutors) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {

                return profileService.requestUpdatePhone(sharedPreferencesHelper.getUserToken(), sharedPreferencesHelper.getPhone());
            }
        }.asLiveData();
    }

    public boolean isSameNumberPhone(String phone) {
        return phone.equals(sharedPreferencesHelper.getPhone());
    }
}
