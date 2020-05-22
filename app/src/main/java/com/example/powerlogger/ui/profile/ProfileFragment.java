package com.example.powerlogger.ui.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.ProfileFragmentBindingImpl;
import com.example.powerlogger.dto.user.UserDTO;
import com.example.powerlogger.utils.StringUtils;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private ProfileFragmentBindingImpl binding;

    private static boolean stringNotEmpty(String errorValue) {
        return errorValue != null && errorValue.length() > 0;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        binding.setView(this);
        binding.setViewmodel(viewModel);

        viewModel.updateDtoWithCurrentProfile();
        viewModel.getErrorsLiveData().observe(getViewLifecycleOwner(), this::errorsChanged);

        return binding.getRoot();
    }

    public void onConfirm() {
        viewModel.sendProfileUpdates(this::onSuccess, this::onFail);
    }

    private void onSuccess(UserDTO o) {
        getParentFragmentManager().popBackStack();
    }

    private void onFail(Throwable t) {
        getParentFragmentManager().popBackStack();
        Toast.makeText(getContext(), R.string.something_wrong_happened, Toast.LENGTH_SHORT).show();
    }

    private void errorsChanged(Map<String, String> errorsMap) {
        binding.registerUsername.setError(errorsMap.get("username"));
        binding.registerEmail.setError(errorsMap.get("email"));
        binding.userWeight.setError(errorsMap.get("weight"));
        binding.userHeight.setError(errorsMap.get("height"));
        binding.userBirthDate.setError(errorsMap.get("birthDate"));

        // Check if data valid
        enableButtonIfFieldsOk();
    }

    private void enableButtonIfFieldsOk() {
        boolean allNull = viewModel.getErrorsLiveData().getValue()
                .values()
                .stream().noneMatch(ProfileFragment::stringNotEmpty);

        binding.saveProfileData.setEnabled(allNull);
    }
}

