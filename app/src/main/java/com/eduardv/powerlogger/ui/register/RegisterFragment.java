package com.eduardv.powerlogger.ui.register;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentRegisterBinding;
import com.eduardv.powerlogger.ui.userSettings.UserSettingsFragment;
import com.eduardv.powerlogger.utils.StringUtils;

public class RegisterFragment extends Fragment {
    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = binding.getRoot();
        binding.setViewmodel(registerViewModel);
        binding.setLifecycleOwner(this);
        registerViewModel.getRegisterFormStateMutableLiveData().observe(getViewLifecycleOwner(), this::onChanged);

        binding.toUserSettingsButton.setOnClickListener(this::onConfirmClick);

        return view;
    }

    private boolean emptyInput(EditText e) {
        return StringUtils.isEmpty(e.getText().toString());
    }

    private void onConfirmClick(View v) {
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.register_fragment_container, userSettingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void onChanged(RegisterFormState state) {
        if (!emptyInput(binding.registerUsername) && state.getUsernameError() != null) {
            binding.registerUsernameLayout.setError(state.getUsernameError());
        } else {
            binding.registerUsernameLayout.setError(null);
        }

        if (!emptyInput(binding.registerPassword) && state.getPasswordError() != null) {
            binding.registerPasswordLayout.setError(state.getPasswordError());
        } else {
            binding.registerPasswordLayout.setError(null);
        }

        if (!emptyInput(binding.registerConfirmPassword) && state.getConfirmPasswordError() != null) {
            binding.registerConfirmPasswordLayout.setError(state.getConfirmPasswordError());
        } else {
            binding.registerConfirmPasswordLayout.setError(null);
        }

        if (!emptyInput(binding.registerEmail) && state.getEmailError() != null) {
            binding.registerEmailLayout.setError(state.getEmailError());
        } else {
            binding.registerEmailLayout.setError(null);
        }

        if (state.isDataValid()) {
            binding.toUserSettingsButton.setEnabled(true);
        } else {
            binding.toUserSettingsButton.setEnabled(false);
        }
    }
}
