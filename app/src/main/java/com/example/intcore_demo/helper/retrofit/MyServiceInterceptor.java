package com.example.intcore_demo.helper.retrofit;

import android.util.Log;

import com.example.intcore_demo.helper.core.SharedPreferencesHelper;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mohamed Khaled on Sun, 12/Aug/2018 at 2:55 PM.
 * <p>
 * mohamed.khaled@apptcom.com
 * linkedin.com/in/mohamed5aled
 */

/**
 * Interceptor which adds headers from shared preferences according to the added custom headers,
 * Authentication, languageCode and level headers by default.
 * <br>
 * when No-Authentication or Single-Language header is set to true add Authentication and multi
 * language headers from prefs
 */
@Singleton
public class MyServiceInterceptor implements Interceptor {
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    private String userToken;

    private Request.Builder requestBuilder;

    @Inject
    MyServiceInterceptor() {
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        requestBuilder = request.newBuilder();
        Log.i("MyServiceInterceptor", "in MyServiceInterceptor " + request.headers().get("No-Authentication"));
        if (request.header("No-Authentication") == null || "false".equalsIgnoreCase(request.headers().get("No-Authentication"))) {
            addAuthenticationHeader();
            requestBuilder.removeHeader("No-Authentication");

        } else
            requestBuilder.removeHeader("No-Authentication");


        return chain.proceed(requestBuilder.build());
    }

    private void addAuthenticationHeader() {
        if (userToken == null) {
            userToken = sharedPreferencesHelper.getUserToken();

            if (userToken == null)
                Log.e("MyServiceInterceptor", "Null user token in API call that requires authentication");
            else
                requestBuilder.addHeader("Authorization", "Bearer {" + userToken + "}");

        } else {
            requestBuilder.addHeader("Authorization", "Bearer {" + userToken + "}");
        }
    }

}