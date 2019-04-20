package com.example.intcore_demo.login.viewmodel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.helper.utilities.Validation;
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

    private final SingleLiveEvent<String> profileIdSingleLiveEvent;
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

        profileIdSingleLiveEvent = new SingleLiveEvent<>();
        showNoNetworkScreenEvent = new MutableLiveData<>();
        dataLoading = new MutableLiveData<>();
        jsonElementObserver = getJsonElementObserver();
        errorMessageEvent = new SingleLiveEvent<>();

    }

    public void getSignUp() {
        SignUpBody body = new SignUpBody();
        body.setEmail(getMutableEmailLiveData().getValue());
        body.setPassword(getMutablePasswordLiveData().getValue());
        body.setName(getMutableNameLiveData().getValue());
        body.setPhone(getMutablePhoneLiveData().getValue());
        jsonElementLiveData = logInRepository.getSignUp(body);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void getSignIn() {
        SignInBody body = new SignInBody();
        body.setName(getMutableEmailLiveData().getValue());
        body.setPassword(getMutablePasswordLiveData().getValue());
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
                            if (resource.getData().getAsJsonObject().getAsJsonObject("user").has("id")) {
                                profileIdSingleLiveEvent.setValue(resource.getData().getAsJsonObject().getAsJsonObject("user").get("api_token").getAsString());
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

    private String validEmailAtSignIn() {
        if (getMutableEmailLiveData().getValue() != null && !getMutableEmailLiveData().getValue().trim().isEmpty()) {
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

    private String validPhoneNumber() {
        if (getMutablePhoneLiveData().getValue() != null && !getMutablePhoneLiveData().getValue().trim().isEmpty()
                && Validation.isValidPhoneNumber(getMutableEmailLiveData().getValue())) {
            return getMutableEmailLiveData().getValue();
        }
        return null;
    }

    private String validPassword() {
        if (mutablePasswordLiveData.getValue() != null && !mutablePasswordLiveData.getValue().trim().isEmpty()) {
            return mutablePasswordLiveData.getValue();
        }
        return null;
    }

    private String validPasswordSignUp() {
        if (mutablePasswordLiveData.getValue() != null && !mutablePasswordLiveData.getValue().trim().isEmpty()) {
            if (mutableRePasswordLiveData.getValue() != null && !mutableRePasswordLiveData.getValue().trim().isEmpty()) {
                if (mutablePasswordLiveData.getValue().equals(mutableRePasswordLiveData.getValue())) {
                    return mutablePasswordLiveData.getValue();
                }
            }
        }
        return null;
    }

    public boolean isValidSignUp() {
        return validEmailAtSignUp() != null && validPasswordSignUp() != null && validPhoneNumber() != null;
    }

    public boolean isValidLogin() {
        return validEmailAtSignIn() != null && validPassword() != null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (jsonElementLiveData != null)
            jsonElementLiveData.removeObserver(jsonElementObserver);
    }

    public MutableLiveData<String> getMutableProfileResponseLiveData() {
        return profileIdSingleLiveEvent;
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

    public SingleLiveEvent<String> getProfileIdSingleLiveEvent() {
        return profileIdSingleLiveEvent;
    }
}
