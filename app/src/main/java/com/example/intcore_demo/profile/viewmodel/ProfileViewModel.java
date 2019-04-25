package com.example.intcore_demo.profile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.intcore_demo.App;
import com.example.intcore_demo.helper.livedata.Resource;
import com.example.intcore_demo.helper.livedata.SingleLiveEvent;
import com.example.intcore_demo.helper.utilities.Validation;
import com.example.intcore_demo.profile.model.User;
import com.example.intcore_demo.profile.model.repo.ProfileRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> mutableEmailLiveData;
    private final MutableLiveData<String> mutableNameLiveData;
    private final MutableLiveData<String> mutablePhoneLiveData;
    private final MutableLiveData<String> mutableImageLiveData;

    private LiveData<Resource<User>> profileResponseLiveData;
    private Observer<Resource<User>> profileResponseObserver;
    private final MutableLiveData<Boolean> dataLoading;
    private final MutableLiveData<Boolean> showNoNetworkScreenEvent;
    private final SingleLiveEvent<String> errorMessageEvent;

    private LiveData<Resource<JsonElement>> jsonElementLiveData;
    private Observer<Resource<JsonElement>> jsonElementObserver;
    private SingleLiveEvent<String> observeCodePhone;
    @Inject
    ProfileRepository profileRepository;

    public ProfileViewModel() {
        App.getAppComponent().inject(this);

        mutableEmailLiveData = new MutableLiveData<>();
        mutableNameLiveData = new MutableLiveData<>();
        mutablePhoneLiveData = new MutableLiveData<>();
        mutableImageLiveData = new MutableLiveData<>();
        observeCodePhone = new SingleLiveEvent<>();
        profileResponseObserver = getProfileResponse();
        errorMessageEvent = new SingleLiveEvent<>();
        dataLoading = new MutableLiveData<>();
        showNoNetworkScreenEvent = new MutableLiveData<>();

        jsonElementObserver = getJsonElementObserver();

    }

    public boolean isValidUpdateProfile(String email, String password, String name) {

        if (email == null || email.trim().isEmpty() || !Validation.isValidEmailAddress(email)) {
            errorMessageEvent.setValue("you must write your right Email ");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            errorMessageEvent.setValue("you must write your right name ");
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
        return true;
    }

    public boolean isValidPassword(String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.trim().isEmpty() || oldPassword.length() < 7) {
            errorMessageEvent.setValue("you must write your right old password ");
            return false;
        }

        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 7) {
            errorMessageEvent.setValue("new password must  more than 8 character ");
            return false;
        }

        if (oldPassword.equals(oldPassword.toLowerCase()) || oldPassword.equals(oldPassword.toUpperCase())) {
            errorMessageEvent.setValue(" old Password must include small and capital letter");
            return false;
        }
        if (newPassword.equals(newPassword.toLowerCase()) || newPassword.equals(newPassword.toUpperCase())) {
            errorMessageEvent.setValue(" new Password must include small and capital letter");
            return false;
        }
        if (newPassword.equals(oldPassword)) {
            errorMessageEvent.setValue("new password must  not match old password ");
            return false;
        }
        return true;
    }

    public boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty() || Validation.isValidPhoneNumber(phone)) {
            errorMessageEvent.setValue("you must write your  right  number phone");
            return false;
        }
        if (profileRepository.isSameNumberPhone(phone)) {
            errorMessageEvent.setValue("new number phone must not match old number");
            return false;
        }
        return true;
    }

    public void changePassword(String oldPassword, String newPassword) {
        jsonElementLiveData = profileRepository.updatePassword(newPassword, oldPassword);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void updateNumberPhone(String phone, String codePhone) {
        jsonElementLiveData = profileRepository.updateNumberPhone(phone, codePhone);
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void requestUpdateNumberPhone() {
        jsonElementLiveData = profileRepository.requestUpdateNumberPhone();
        jsonElementLiveData.observeForever(jsonElementObserver);
    }

    public void getProfile() {
        profileResponseLiveData = profileRepository.getProfile();
        profileResponseLiveData.observeForever(profileResponseObserver);
    }

    public void logout() {
        profileRepository.Logout();
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
                            try {
                                if (resource.getData().getAsJsonObject().has("message")) {
                                    errorMessageEvent.setValue(resource.getData().getAsJsonObject().get("message").toString());
                                }

                                if (resource.getData().getAsJsonObject().getAsJsonObject("user") != null && resource.getData().getAsJsonObject().getAsJsonObject("user").has("id")) {
                                    errorMessageEvent.setValue("profile is successful updated");
                                }

                                if (resource.getData().getAsJsonObject().has("code")) {
                                    observeCodePhone.setValue(resource.getData().getAsJsonObject().get("code").toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
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
                            mutableNameLiveData.setValue(resource.getData().getName());
                            mutableEmailLiveData.setValue(resource.getData().getEmail());
                            mutableImageLiveData.setValue(resource.getData().getImage());
                            mutablePhoneLiveData.setValue(resource.getData().getPhone());
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

    public void getUpdateProfile(String name, String email, String image) {
        jsonElementLiveData = profileRepository.updateProfile(name, email, image);
        jsonElementLiveData.observeForever(jsonElementObserver);
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

    public MutableLiveData<String> getMutablePhoneLiveData() {
        return mutablePhoneLiveData;
    }

    public MutableLiveData<String> getMutableImageLiveData() {
        return mutableImageLiveData;
    }

    public SingleLiveEvent<String> getObserveCodePhone() {
        return observeCodePhone;
    }

    public void setObserveCodePhone(SingleLiveEvent<String> observeCodePhone) {
        this.observeCodePhone = observeCodePhone;
    }
}
