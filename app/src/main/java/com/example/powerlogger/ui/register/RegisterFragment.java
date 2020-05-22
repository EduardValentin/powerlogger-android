package com.example.powerlogger.ui.register;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentRegisterBinding;
import com.example.powerlogger.ui.userSettings.UserSettingsFragment;
import com.example.powerlogger.utils.StringUtils;

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
            binding.registerUsername.setError(state.getUsernameError());
        }
        if (!emptyInput(binding.registerPassword) && state.getPasswordError() != null) {
            binding.registerPassword.setError(state.getPasswordError());
        }

        if (!emptyInput(binding.registerConfirmPassword) && state.getConfirmPasswordError() != null) {
            binding.registerConfirmPassword.setError(state.getConfirmPasswordError());
        }

        if (!emptyInput(binding.registerEmail) && state.getEmailError() != null) {
            binding.registerEmail.setError(state.getEmailError());
        }

        if (state.isDataValid()) {
            binding.toUserSettingsButton.setEnabled(true);
        } else {
            binding.toUserSettingsButton.setEnabled(false);
        }
    }
}
