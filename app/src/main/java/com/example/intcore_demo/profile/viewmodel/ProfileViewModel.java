package com.example.intcore_demo.profile.viewmodel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.core.SharedPreferencesHelper;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.helper.utilities.Validation;
import com.example.intcore_demo.profile.model.User;
import com.example.intcore_demo.profile.model.repo.ProfileRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> mutableEmailLiveData;
    private final MutableLiveData<String> mutableNameLiveData;
    private final MutableLiveData<String> mutablePasswordLiveData;
    private final MutableLiveData<String> mutablePhoneLiveData;
    private final MutableLiveData<String> mutableImageLiveData;

    private final MutableLiveData<User> mutableProfileResponseLiveData;
    private LiveData<Resource<User>> profileResponseLiveData;
    private Observer<Resource<User>> profileResponseObserver;
    private final MutableLiveData<Boolean> dataLoading;
    private final MutableLiveData<Boolean> showNoNetworkScreenEvent;
    private final SingleLiveEvent<String> errorMessageEvent;

    private LiveData<Resource<JsonElement>> jsonElementLiveData;
    private Observer<Resource<JsonElement>> jsonElementObserver;

    @Inject
    ProfileRepository profileRepository;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    public ProfileViewModel() {
        App.getAppComponent().inject(this);

        mutableEmailLiveData = new MutableLiveData<>();
        mutableNameLiveData = new MutableLiveData<>();
        mutablePasswordLiveData = new MutableLiveData<>();
        mutablePhoneLiveData = new MutableLiveData<>();
        mutableImageLiveData = new MutableLiveData<>();

        mutableProfileResponseLiveData = new MutableLiveData<>();
        profileResponseObserver = getProfileResponse();
        errorMessageEvent = new SingleLiveEvent<>();
        dataLoading = new MutableLiveData<>();
        showNoNetworkScreenEvent = new MutableLiveData<>();

        jsonElementObserver = getJsonElementObserver();

    }

    private String validPassword() {
        if (mutablePasswordLiveData.getValue() != null && !mutablePasswordLiveData.getValue().trim().isEmpty()) {
            return mutablePasswordLiveData.getValue();
        }
        return null;
    }

    private String validPhoneNumber() {
        if (getMutablePhoneLiveData().getValue() != null && !getMutablePhoneLiveData().getValue().trim().isEmpty()
                && Validation.isValidPhoneNumber(getMutableEmailLiveData().getValue())) {
            return getMutableEmailLiveData().getValue();
        }
        return null;
    }

    private String validEmailAtSignUp() {
        if (getMutableEmailLiveData().getValue() != null && !getMutableEmailLiveData().getValue().trim().isEmpty()
                && Validation.isValidEmailAddress(getMutableEmailLiveData().getValue())) {
            return getMutableEmailLiveData().getValue();
        }
        return null;
    }

    public boolean isValidSignUp() {
        return validEmailAtSignUp() != null && validPhoneNumber() != null;
    }

    public void getProfile(String profileToken) {
        profileResponseLiveData = profileRepository.getProfile(profileToken);
        profileResponseLiveData.observeForever(profileResponseObserver);
    }

    private Observer<Resource<JsonElement>> getJsonElementObserver() {

        return resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        showNoNetworkScreenEvent.setValue(false);
                        break;
                    case SUCCESS:
                        dataLoading.setValue(false);
                        if (resource.getData() != null) {
                            if (resource.getData().getAsJsonObject().get("errors") != null) {
                                for (int i = 0; i < resource.getData().getAsJsonObject().getAsJsonObject("errors").getAsJsonArray().size(); i++) {
                                    errorMessageEvent.setValue(resource.getData().getAsJsonObject().getAsJsonObject("errors").getAsJsonArray().get(i).getAsJsonObject().get("message").toString());
                                }
                            }

                            showNoNetworkScreenEvent.setValue(false);
                        } else
                            showNoNetworkScreenEvent.setValue(true);
                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        errorMessageEvent.setValue(resource.message);
                        break;

                }
            }
        };

    }

    public void getUpdateProfile(String token, String name, String email, String image) {
        jsonElementLiveData = profileRepository.updateProfile(token, name, email, image);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public MutableLiveData<Boolean> getShowNoNetworkScreenEvent() {
        return showNoNetworkScreenEvent;
    }

    private Observer<Resource<User>> getProfileResponse() {

        return resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        showNoNetworkScreenEvent.setValue(false);

                        break;
                    case SUCCESS:
                        dataLoading.setValue(false);
                        if (resource.getData() != null) {
                            mutableProfileResponseLiveData.setValue(resource.getData());
                            sharedPreferencesHelper.setUserId(resource.getData().getId());
                            sharedPreferencesHelper.setUserToken(resource.getData().getApiToken());
                            showNoNetworkScreenEvent.setValue(false);

                        } else
                            showNoNetworkScreenEvent.setValue(true);
                        errorMessageEvent.setValue(resource.message);

                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        errorMessageEvent.setValue(resource.message);
                        break;

                }
            }
        };
    }

    public SingleLiveEvent<String> getErrorMessageEvent() {
        return errorMessageEvent;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (profileResponseLiveData != null)
            profileResponseLiveData.removeObserver(profileResponseObserver);
        if (jsonElementLiveData != null)
            jsonElementLiveData.removeObserver(jsonElementObserver);
    }

    public MutableLiveData<String> getMutableEmailLiveData() {
        return mutableEmailLiveData;
    }

    public MutableLiveData<String> getMutableNameLiveData() {
        return mutableNameLiveData;
    }

    public MutableLiveData<String> getMutablePasswordLiveData() {
        return mutablePasswordLiveData;
    }

    public MutableLiveData<String> getMutablePhoneLiveData() {
        return mutablePhoneLiveData;
    }

    public MutableLiveData<String> getMutableImageLiveData() {
        return mutableImageLiveData;
    }
}
