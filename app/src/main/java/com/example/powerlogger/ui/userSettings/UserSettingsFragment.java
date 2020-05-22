package com.example.powerlogger.ui.userSettings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentUserSettingsBinding;
import com.example.powerlogger.ui.DatePickerFragment;
import com.example.powerlogger.ui.logger.LogConstants;
import com.example.powerlogger.ui.register.RegisterViewModel;
import com.example.powerlogger.ui.register.UserConstants;
import com.example.powerlogger.utils.StringUtils;

public class UserSettingsFragment extends Fragment {

    private UserSettingsViewModel userSettingsViewModel;
    private FragmentUserSettingsBinding binding;
    private UserSettingsActionsListener listener;

    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userSettingsViewModel = ViewModelProviders.of(this).get(UserSettingsViewModel.class);

        try {
            username = getArguments().getString("username");
        } catch (NullPointerException e) {
            Log.e("USER_SETTINGS_PARSE", "Can't parse username.");
            username = "";
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_settings, container, false);

        binding.setViewmodel(userSettingsViewModel);
        binding.setFragment(this);
        binding.setLifecycleOwner(this);

        binding.registerButton.setOnClickListener(this::onConfirm);
        binding.userBirthDate.setOnClickListener(this::openDatePicker);

        userSettingsViewModel.getSettingsFormErrors().observe(getViewLifecycleOwner(), errors -> {
            if (!StringUtils.isEmpty(binding.userWeight.getText().toString()) && errors.get(UserConstants.USER_WEIGHT_ERR) != null) {
                binding.userWeight.setError(errors.get(UserConstants.USER_WEIGHT_ERR));
            }

            if (!StringUtils.isEmpty(binding.userHeight.getText().toString()) && errors.get(UserConstants.USER_HEIGHT_ERR) != null) {
                binding.userHeight.setError(errors.get(UserConstants.USER_HEIGHT_ERR));
            }

            if (!StringUtils.isEmpty(binding.userBirthDate.getText().toString()) && errors.get(UserConstants.USER_BIRTH_DATE_ERR) != null) {
                binding.userBirthDate.setError(errors.get(UserConstants.USER_BIRTH_DATE_ERR));
            }

            if (userSettingsViewModel.isUserSettingsDataValid()) {
                binding.registerButton.setEnabled(true);
            } else {
                binding.registerButton.setEnabled(false);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (UserSettingsActionsListener) context;
    }

    public void onConfirm(View v) {
        listener.onConfirmSettings(username, userSettingsViewModel.getUserSettingsDTO());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openDatePicker(View v) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "date picker");
    }

    private void onSignUpResponse() {
        binding.loadingSpinner.setVisibility(View.INVISIBLE);
    }
}
