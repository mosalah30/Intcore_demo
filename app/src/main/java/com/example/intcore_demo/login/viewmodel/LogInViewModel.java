package com.example.intcore_demo.login.viewmodel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.login.model.SignInBody;
import com.example.intcore_demo.login.model.SignUpBody;
import com.example.intcore_demo.login.model.repo.LogInRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel {

    private final MutableLiveData<String> mutableEmailLiveData;
    private final MutableLiveData<String> mutableNameLiveData;
    private final MutableLiveData<String> mutablePasswordLiveData;
    private final MutableLiveData<String> mutableRePasswordLiveData;
    private final MutableLiveData<String> mutablePhoneLiveData;

    private final MutableLiveData<JsonElement> mutableJsonElementLiveData;
    private LiveData<Resource<JsonElement>> jsonElementLiveData;
    private Observer<Resource<JsonElement>> jsonElementObserver;
    private final MutableLiveData<Boolean> dataLoading;
    private final MutableLiveData<Boolean> showNoNetworkScreenEvent;
    private final SingleLiveEvent<String> errorMessageEvent;
    @Inject
    LogInRepository logInRepository;

    public LogInViewModel() {
        App.getAppComponent().inject(this);
        mutableEmailLiveData = new MutableLiveData<>();
        mutableNameLiveData = new MutableLiveData<>();
        mutablePasswordLiveData = new MutableLiveData<>();
        mutableRePasswordLiveData = new MutableLiveData<>();
        mutablePhoneLiveData = new MutableLiveData<>();

        mutableJsonElementLiveData = new MutableLiveData<>();
        showNoNetworkScreenEvent = new MutableLiveData<>();
        dataLoading = new MutableLiveData<>();
        jsonElementObserver = getProfileResponse();
        errorMessageEvent = new SingleLiveEvent<>();

    }

    public void getSignUp(SignUpBody body) {
        jsonElementLiveData = logInRepository.getSignUp(body);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void getSignIn(SignInBody body) {
        jsonElementLiveData = logInRepository.getSignIn(body);
        jsonElementLiveData.observeForever(jsonElementObserver);
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

    public MutableLiveData<String> getMutableRePasswordLiveData() {
        return mutableRePasswordLiveData;
    }

    public MutableLiveData<String> getMutablePhoneLiveData() {
        return mutablePhoneLiveData;
    }

    private Observer<Resource<JsonElement>> getProfileResponse() {

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
                            mutableJsonElementLiveData.setValue(resource.getData());
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

    private String isValidEmailAtSignUp() {
        if (getMutableEmailLiveData().getValue() != null && !getMutableEmailLiveData().getValue().trim().isEmpty()) {
            return getMutableEmailLiveData().getValue();
        }
        return null;
    }

    private String isValidPassword() {
        if (mutablePasswordLiveData.getValue() != null && !mutablePasswordLiveData.getValue().trim().isEmpty()) {
            return mutablePasswordLiveData.getValue();
        }
        return null;
    }

    public boolean isLogin() {
        return isValidEmailAtSignUp() != null && isValidPassword() != null;
    }

    @Override

    protected void onCleared() {
        super.onCleared();
        if (jsonElementLiveData != null)
            jsonElementLiveData.removeObserver(jsonElementObserver);
    }

    public MutableLiveData<JsonElement> getMutableProfileResponseLiveData() {
        return mutableJsonElementLiveData;
    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public MutableLiveData<Boolean> getShowNoNetworkScreenEvent() {
        return showNoNetworkScreenEvent;
    }

    public SingleLiveEvent<String> getErrorMessageEvent() {
        return errorMessageEvent;
    }

}
