package com.example.powerlogger.ui.register;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentRegisterBinding;
import com.example.powerlogger.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private RegisterViewModel registerViewModel;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        // Inflate the layout for this fragment

        FragmentRegisterBinding fragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = fragmentRegisterBinding.getRoot();
        fragmentRegisterBinding.setViewmodel(registerViewModel);
        fragmentRegisterBinding.setLifecycleOwner(this);

        final EditText username = view.findViewById(R.id.registerUsername);
        final EditText email = view.findViewById(R.id.registerEmail);
        final EditText password = view.findViewById(R.id.registerPassword);
        final EditText confirmPassword = view.findViewById(R.id.registerConfirmPassword);
        final Button toUserSettingsBtn = view.findViewById(R.id.toUserSettingsButton);


        registerViewModel.getRegisterFormStateMutableLiveData().observe(this, (RegisterFormState state) -> {
            if(!emptyInput(username) && state.getUsernameError() != null) {
                username.setError(state.getUsernameError());
            }
            if(!emptyInput(password) && state.getPasswordError() != null) {
                password.setError(state.getPasswordError());
            }

            if(!emptyInput(confirmPassword) && state.getConfirmPasswordError() != null) {
                confirmPassword.setError(state.getConfirmPasswordError());
            }

            if(!emptyInput(email) && state.getEmailError() != null) {
                email.setError(state.getEmailError());
            }

            if(state.isDataValid()) {
                toUserSettingsBtn.setEnabled(true);
            } else {
                toUserSettingsBtn.setEnabled(false);
            }
        });


        toUserSettingsBtn.setOnClickListener(v -> {
            UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.register_fragment_container, userSettingsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;

    }

    private boolean emptyInput(EditText e) {
        return StringUtils.isEmpty(e.getText().toString());
    }
}
