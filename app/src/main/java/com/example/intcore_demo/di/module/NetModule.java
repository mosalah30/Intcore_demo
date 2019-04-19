package com.example.intcore_demo.di.module;


import android.app.Application;

import com.example.intcore_demo.BuildConfig;
import com.example.intcore_demo.helper.retrofit.LiveDataCallAdapterFactory;
import com.example.intcore_demo.helper.retrofit.MyServiceInterceptor;
import com.example.intcore_demo.helper.retrofit.NullOnEmptyConverterFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetModule {
    final private String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public Cache provideHttpCache(Application application) {
        long cacheSize = 10 * 1024 * 1024L; //10 MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setPrettyPrinting();
        return gsonBuilder.create();
    }


    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Cache cache, MyServiceInterceptor myServiceInterceptor, HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(myServiceInterceptor);
        builder.addNetworkInterceptor(interceptor);
        builder.cache(cache);
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        if (BuildConfig.DEBUG)
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            return new HttpLoggingInterceptor();
    }
}
