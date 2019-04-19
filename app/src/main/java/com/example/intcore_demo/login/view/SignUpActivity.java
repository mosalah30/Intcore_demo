package com.example.intcore_demo.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.ActivitySignupBinding;
import com.example.intcore_demo.login.viewmodel.LogInViewModel;
import com.example.intcore_demo.profile.view.ProfileActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private LogInViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        viewModel = ViewModelProviders.of(this).get(LogInViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
//        viewModel.getSignUp();
        subscribeForMessagesError();

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
