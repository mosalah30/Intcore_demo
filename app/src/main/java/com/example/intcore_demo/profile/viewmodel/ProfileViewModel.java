package com.example.intcore_demo.profile.viewmodel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.profile.model.ProfileResponse;

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

        return listResource -> {
            if (listResource != null) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        showNoNetworkScreenEvent.setValue(false);

                        break;
                    case SUCCESS:
                        dataLoading.setValue(false);
                        if (listResource.getData() != null) {
                            mutableProfileResponseLiveData.setValue(listResource.getData());
                            showNoNetworkScreenEvent.setValue(false);

                        } else
                            showNoNetworkScreenEvent.setValue(true);

                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        errorMessageEvent.setValue(listResource.message);
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
