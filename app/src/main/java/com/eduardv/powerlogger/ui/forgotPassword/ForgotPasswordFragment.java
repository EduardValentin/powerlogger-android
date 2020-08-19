package com.eduardv.powerlogger.ui.forgotPassword;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.ForgotPasswordFragmentBinding;
import com.eduardv.powerlogger.databinding.ForgotPasswordFragmentBindingImpl;

import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment {

    private ForgotPasswordViewModel mViewModel;
    private ForgotPasswordFragmentBindingImpl binding;


    private TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable email) {
            String emailString = email.toString();

            if (!mViewModel.validateMail(emailString)) {
                binding.emailLayout.setError("Email invalid");
                binding.sendForgotEmail.setEnabled(false);
            } else {
                binding.emailLayout.setError(null);
                binding.sendForgotEmail.setEnabled(true);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.forgot_password_fragment, container, false);

        binding.setView(this);
        binding.setModel(mViewModel);

        return binding.getRoot();
    }

    public void onSubmit() {
        mViewModel.sendResetPasswordEmail(this::onSuccess, this::onError);
    }

    public void onSuccess(Void v) {
        binding.message.setText("Please open the email and follow the instructions in order to reset your password");
    }

    public void onError(Throwable t) {
        binding.message.setText("We couldn't send an email to you. Please try again later");
    }

    public TextWatcher getAfterTextChangedListener() {
        return afterTextChangedListener;
    }
}