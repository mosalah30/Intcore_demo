package com.example.intcore_demo.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.ActivityProfileBinding;
import com.example.intcore_demo.login.view.SignInActivity;
import com.example.intcore_demo.profile.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {
    ProfileViewModel viewModel;
    ActivityProfileBinding binding;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.profile);
        setSupportActionBar(toolbar);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        getProfile();
        subscribeForMessagesError();
        binding.btnUpdateProfile.setOnClickListener(v -> updateProfile());
        binding.btnChangPassword.setOnClickListener(v -> showChangePasswordDialog());
        binding.btnUpdatePhone.setOnClickListener(v -> requestPhoneCode());
        updatePhone();
    }

    private void getProfile() {
        viewModel.getProfile();
    }

    private void updatePhone() {

        phone = binding.etPhone.getText().toString();
        viewModel.getObserveCodePhone().observe(this, requestPhoneCode -> {
            if (requestPhoneCode != null)
                viewModel.updateNumberPhone(phone, requestPhoneCode);
        });

    }

    private void updateProfile() {
        String name = binding.etName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etName.getText().toString();
        String image = "image.bng";

        if (viewModel.isValidUpdateProfile(email, password, name)) {
            viewModel.getUpdateProfile(name, email, image);
        }
    }

    private void requestPhoneCode() {
        phone = binding.etPhone.getText().toString();

        if (viewModel.isValidPhone(phone)) {

            viewModel.requestUpdateNumberPhone();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                if (result != null) {
//                    Uri resultUri = result.getUri();
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error ;
//                if (result != null) {
//                    error = result.getError();
//                    error.printStackTrace();
//                }
//
//            }
//        }
//    }

    private void subscribeForMessagesError() {
        viewModel.getErrorMessageEvent().observe(this, messageText -> Toast.makeText(this, messageText, Toast.LENGTH_LONG).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.btn_menu_logout == item.getItemId()) {
            viewModel.logout();
            navigateToLogInActivity();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToLogInActivity() {

        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showChangePasswordDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ChangePasswordDialog editNameDialogFragment = new ChangePasswordDialog();
        editNameDialogFragment.show(fm, "fragment_change_password");
    }
}


