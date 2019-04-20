package com.example.intcore_demo.profile.view;

import android.os.Bundle;
import android.widget.Toast;

import com.example.intcore_demo.Constants;
import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.ActivityProfileBinding;
import com.example.intcore_demo.profile.viewmodel.ProfileViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ProfileActivity extends AppCompatActivity {
    ProfileViewModel viewModel;
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        getProfile();
        subscribeForMessagesError();
    }

    private void getProfile() {
        if (getIntent().getExtras() != null) {
            viewModel.getProfile(getIntent().getExtras().getString(Constants.Token_ID_KEY));
        }
    }


    private void subscribeForMessagesError() {
        viewModel.getErrorMessageEvent().observe(this, messageText -> Toast.makeText(this, messageText, Toast.LENGTH_LONG).show());
    }


}
