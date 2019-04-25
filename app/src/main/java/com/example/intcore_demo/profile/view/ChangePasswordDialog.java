package com.example.intcore_demo.profile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.intcore_demo.R;
import com.example.intcore_demo.databinding.FragmentDialogChangPasswordBinding;
import com.example.intcore_demo.profile.viewmodel.ProfileViewModel;

public class ChangePasswordDialog extends DialogFragment {
    FragmentDialogChangPasswordBinding binding;
    ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_chang_password, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.btnChangeOldPassword.setOnClickListener(v -> {
            updatePassword();
        });
        return binding.getRoot();
    }

    private void updatePassword() {
        String oldPassword = binding.etOldPassword.getText().toString();
        String newPassword = binding.etNewPassword.getText().toString();
        if (viewModel.isValidPassword(oldPassword, newPassword)) {
            viewModel.changePassword(oldPassword, newPassword);
            dismiss();
        }
    }
}
