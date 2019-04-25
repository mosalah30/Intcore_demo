package com.example.intcore_demo.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.ActivityLoginBinding;
import com.example.intcore_demo.login.viewmodel.LogInViewModel;
import com.example.intcore_demo.profile.view.ProfileActivity;

public class SignInActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    LogInViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LogInViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        if (viewModel.sharedPreferencesHelper.isUserLoggedIn()) {
            navigateToProfileActivity();
            finish();
        }
        binding.btnLogIn.setOnClickListener(v -> signIn());
        binding.btnSignup.setOnClickListener(v -> navigateToSignUpActivity());
        subscribeForMessagesError();
    }

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void signIn() {
        String password = binding.etPassword.getText().toString();
        String email = binding.etEmail.getText().toString();
        if (viewModel.isValidSignIn(email, password)) {
            viewModel.getSignIn(email, password);
            viewModel.getIsUserLoginSingleLiveEvent().observe(this, isLogin -> {
                if (isLogin != null && isLogin) {
                    navigateToProfileActivity();
                }
            });
        }
    }

    private void subscribeForMessagesError() {
        viewModel.getErrorMessageEvent().observe(this, messageText -> Toast.makeText(this, messageText, Toast.LENGTH_LONG).show());
    }

    private void navigateToProfileActivity() {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
    }


