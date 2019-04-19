package com.example.intcore_demo.di;


import com.example.intcore_demo.di.module.ApiModule;
import com.example.intcore_demo.di.module.AppModule;
import com.example.intcore_demo.di.module.DaoModule;
import com.example.intcore_demo.di.module.NetModule;
import com.example.intcore_demo.login.viewmodel.LogInViewModel;
import com.example.intcore_demo.profile.viewmodel.ProfileViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                NetModule.class,
                DaoModule.class,
                ApiModule.class
        }
)

public interface AppComponent {

    void inject(LogInViewModel logInViewModel);

    void inject(ProfileViewModel profileViewModel);


}
