package com.example.intcore_demo.helper.core;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.Nullable;


/**
 * Created by Mohamed Khaled on Thu, 09/Aug/2018 at 1:01 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */
@Singleton
public class SharedPreferencesHelper {
    public static final String PREF_NAME = "Intcore_prefs";

    private static final String USER_TOKEN = "user_token";
    private static final String DEVICE_TOKEN = "device_token";
    private static final String USER_LOGGED_IN = "user_logged_in";
    private static final String USER_ID = "user_id";



    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void clearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * @return userId saved in prefs, default value is -1
     */
    public int getUserId() {
        return sharedPreferences.getInt(USER_ID, -1);
    }

    public void setUserId(int userId) {
        sharedPreferences.edit().putInt(USER_ID, userId).apply();
    }

    @Nullable
    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, "");
    }

    public void setUserToken(String userToken) {
        sharedPreferences.edit().putString(USER_TOKEN, userToken).apply();
    }

    @Nullable
    public String getDeviceToken() {
        return sharedPreferences.getString(DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String deviceToken) {
        sharedPreferences.edit().putString(DEVICE_TOKEN, deviceToken).apply();
    }

    @Nullable
    public String getDeviceType() {
        return "1";
    }


    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean isUserLoggedIn) {
        sharedPreferences.edit().putBoolean(USER_LOGGED_IN, isUserLoggedIn).apply();
    }

}
