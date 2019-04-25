package com.example.intcore_demo.login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.core.SharedPreferencesHelper;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.helper.utilities.Validation;
import com.example.intcore_demo.login.model.repo.LogInRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

public class LogInViewModel extends ViewModel {


    private LiveData<Resource<JsonElement>> jsonElementLiveData;
    private Observer<Resource<JsonElement>> jsonElementObserver;
    private final SingleLiveEvent<String> errorMessageEvent;
    private SingleLiveEvent<Boolean> isUserLoginSingleLiveEvent;
    @Inject
    public SharedPreferencesHelper sharedPreferencesHelper;
    @Inject
    LogInRepository logInRepository;

    public LogInViewModel() {
        App.getAppComponent().inject(this);

        isUserLoginSingleLiveEvent = new SingleLiveEvent<>();
        jsonElementObserver = getJsonElementObserver();
        errorMessageEvent = new SingleLiveEvent<>();

    }

    public void getSignUp(String email, String password, String name, String phone) {
        jsonElementLiveData = logInRepository.getSignUp(email, password, name, phone);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void getSignIn(String nameOrEmail, String password) {
        jsonElementLiveData = logInRepository.getSignIn(nameOrEmail, password);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }


    private Observer<Resource<JsonElement>> getJsonElementObserver() {

        return resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resource.getData() != null) {

                            if (resource.getData().getAsJsonObject().getAsJsonObject("user").has("id")) {
                                sharedPreferencesHelper.setUserLoggedIn(true);
                                sharedPreferencesHelper.setUserToken(resource.getData().getAsJsonObject().getAsJsonObject("user").get("api_token").getAsString());
                                sharedPreferencesHelper.setPhone(resource.getData().getAsJsonObject().getAsJsonObject("user").get("phone").getAsString());
                                isUserLoginSingleLiveEvent.setValue(true);
                            }
                        }

                        break;
                    case ERROR:
                        errorMessageEvent.setValue(resource.message);

                        break;

                }
            }
        };

    }

    public boolean isValidSignIn(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            errorMessageEvent.setValue("you must write your right Email or right your name");
            return false;
        }

        if (password == null || password.trim().isEmpty() || password.length() < 7) {
            errorMessageEvent.setValue("you must write your right password  more than 8 character ");
            return false;
        }

        if (password.equals(password.toLowerCase()) || password.equals(password.toUpperCase())) {
            errorMessageEvent.setValue("Password must include small and capital letter");
            return false;
        }

        return true;
    }


    public boolean isValidSignUp(String email, String password, String name, String phone, String rePassword) {

        if (email == null || email.trim().isEmpty() || !Validation.isValidEmailAddress(email)) {
            errorMessageEvent.setValue("you must write your right Email ");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            errorMessageEvent.setValue("you must write your right name ");
            return false;
        }

        if (phone == null || phone.trim().isEmpty() || phone.length() < 9) {
            errorMessageEvent.setValue("you must write your right number phone ");
            return false;
        }


        if (password == null || password.trim().isEmpty() || password.length() < 7) {
            errorMessageEvent.setValue("you must write your  password  more than 8 character");
            return false;
        }

        if (password.equals(password.toLowerCase()) || password.equals(password.toUpperCase())) {
            errorMessageEvent.setValue("Password must include small and capital letter");
            return false;
        }
        if (rePassword == null || rePassword.trim().isEmpty() || !password.equals(rePassword)) {
            errorMessageEvent.setValue("password must equal RePassword");
            return false;
        }

        return true;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (jsonElementLiveData != null)
            jsonElementLiveData.removeObserver(jsonElementObserver);
    }


    public SingleLiveEvent<String> getErrorMessageEvent() {
        return errorMessageEvent;
    }


    public SingleLiveEvent<Boolean> getIsUserLoginSingleLiveEvent() {
        return isUserLoginSingleLiveEvent;
    }

    public void setIsUserLoginSingleLiveEvent(SingleLiveEvent<Boolean> isUserLoginSingleLiveEvent) {
        this.isUserLoginSingleLiveEvent = isUserLoginSingleLiveEvent;
    }
}
