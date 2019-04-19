package com.example.intcore_demo.profile.model.repo;

import com.example.intcore_demo.helper.core.AppExecutors;
import com.example.intcore_demo.helper.livedata.ApiResponse;
import com.example.intcore_demo.helper.livedata.NetworkBoundResource;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.utilities.ShouldFetch;
import com.example.intcore_demo.profile.model.ProfileResponse;
import com.example.intcore_demo.profile.model.User;
import com.example.intcore_demo.profile.model.local.ProfileDao;
import com.example.intcore_demo.profile.model.remote.ProfileService;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

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

    public LiveData<Resource<User>> getProfile(String token) {

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
                return profileService.getProfileResponse(token);
            }
        }.asLiveData();
    }
}
