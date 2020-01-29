package com.example.powerlogger.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.MainActivity;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.utils.Result;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        final EditText username = findViewById(R.id.registerUsername);
        final EditText email = findViewById(R.id.registerEmail);
        final EditText password = findViewById(R.id.registerPassword);
        final EditText confirmPassword = findViewById(R.id.registerConfirmPassword);
        final EditText weight = findViewById(R.id.weightTextInput);
        final Button registerButton = findViewById(R.id.registerButton);

        loadingProgressBar = findViewById(R.id.loading);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterFormStateMutableLiveData().observe(this, (RegisterFormState state) -> {
            if(state.getPasswordError() != null) {
                password.setError(state.getPasswordError());
            }

            if(state.getConfirmPasswordError() != null) {
                confirmPassword.setError(state.getConfirmPasswordError());
            }

            if(state.getEmailError() != null) {
                email.setError(state.getEmailError());
            }

            if(state.isDataValid()) {
                registerButton.setEnabled(true);
            } else {
                registerButton.setEnabled(false);
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.valuesChanged(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        confirmPassword.getText().toString(),
                        weight.getText().toString()
                        );
            }
        };

        registerButton.setOnClickListener(view -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                    username.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    weight.getText().toString());

            registerViewModel.signup(registerRequestDTO, obj -> {
                loadingProgressBar.setVisibility(View.INVISIBLE);

                if (obj instanceof Result.Success) {
                    updateUiWithUser();
                    setResult(Activity.RESULT_OK);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = ((Result.Error) obj).getError().getMessage();
                    showRegisterFailed(errorMessage);
                }
            });
        });

        password.addTextChangedListener(watcher);
        confirmPassword.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);
        weight.addTextChangedListener(watcher);
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
