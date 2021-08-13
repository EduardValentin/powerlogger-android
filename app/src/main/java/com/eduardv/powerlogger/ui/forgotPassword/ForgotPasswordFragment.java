package com.eduardv.powerlogger.ui.forgotPassword;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
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
import com.eduardv.powerlogger.databinding.ForgotPasswordFragmentBindingImpl;

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
        binding.loading.setVisibility(View.VISIBLE);

        mViewModel.sendResetPasswordEmail(this::onSuccess, this::onError);
    }

    public void onSuccess(Void v) {
        binding.message.setText("We've sent you an email with instructions to reset your password");
        binding.loading.setVisibility(View.INVISIBLE);
        binding.emailLayout.setVisibility(View.INVISIBLE);

        binding.sendForgotEmail.setText("Back");
        binding.sendForgotEmail.setOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
    }

    public void onError(Throwable t) {
        binding.loading.setVisibility(View.INVISIBLE);
        binding.message.setText("There was a problem sending your email. Please try again later");
    }

    public TextWatcher getAfterTextChangedListener() {
        return afterTextChangedListener;
    }
}