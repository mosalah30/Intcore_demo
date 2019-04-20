package com.example.intcore_demo.profile.viewmodel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.core.SharedPreferencesHelper;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.profile.model.ProfileResponse;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<ProfileResponse> mutableProfileResponseLiveData;
    private LiveData<Resource<ProfileResponse>> profileResponseLiveData;
    private Observer<Resource<ProfileResponse>> profileResponseObserver;
    private final MutableLiveData<Boolean> dataLoading;
    private final MutableLiveData<Boolean> showNoNetworkScreenEvent;

    private final SingleLiveEvent<String> errorMessageEvent;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    public ProfileViewModel() {
        App.getAppComponent().inject(this);
        mutableProfileResponseLiveData = new MutableLiveData<>();
        profileResponseObserver = getProfileResponse();
        errorMessageEvent = new SingleLiveEvent<>();
        dataLoading = new MutableLiveData<>();
        showNoNetworkScreenEvent = new MutableLiveData<>();

    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public MutableLiveData<Boolean> getShowNoNetworkScreenEvent() {
        return showNoNetworkScreenEvent;
    }

    private Observer<Resource<ProfileResponse>> getProfileResponse() {

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
                            sharedPreferencesHelper.setUserId(resource.getData().getUser().getId());
                            sharedPreferencesHelper.setUserToken(resource.getData().getUser().getApiToken());
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
    }
}
