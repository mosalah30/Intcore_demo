package com.example.intcore_demo.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.ActivitySignupBinding;
import com.example.intcore_demo.login.viewmodel.LogInViewModel;
import com.example.intcore_demo.profile.view.ProfileActivity;


public class SignUpActivity extends AppCompatActivity {
    private LogInViewModel viewModel;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        viewModel = ViewModelProviders.of(this).get(LogInViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.btnSignUp.setOnClickListener(v -> signUp());
        subscribeForMessagesError();
    }

    private void signUp() {
        String password = binding.etPassword.getText().toString();
        String email = binding.etEmail.getText().toString();
        String rePassword = binding.etRePassword.getText().toString();
        String phoneNumber = binding.etPhone.getText().toString();
        String name = binding.etName.getText().toString();
        if (viewModel.isValidSignUp(email, password, name, phoneNumber, rePassword)) {
            viewModel.getSignUp(email, password, name, phoneNumber);
            viewModel.getIsUserLoginSingleLiveEvent().observe(this, isLogin -> {
                if (isLogin != null && isLogin) {
                    viewModel.sharedPreferencesHelper.setUserLoggedIn(true);
                    navigateToProfileActivity();
                    finish();
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
            }
}
