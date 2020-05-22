package com.example.powerlogger.ui.register;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.MainActivity;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.user.UserSettingsDTO;
import com.example.powerlogger.ui.userSettings.UserSettingsActionsListener;
import com.example.powerlogger.utils.Result;

import java.time.LocalDate;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, UserSettingsActionsListener {
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        Fragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.register_fragment_container, registerFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);

        registerViewModel.getRegisterRequestDTO().getSettings().setBirthDate(ld.toString());
        ((EditText) findViewById(R.id.userBirthDate)).setText(ld.toString());
    }

    @Override
    public void onConfirmSettings(String username, UserSettingsDTO userSettingsDTO) {
        registerViewModel.getRegisterRequestDTO().setSettings(userSettingsDTO);
        registerViewModel.signup(this::onSignUpCallback);
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void onSignUpCallback(Object obj) {
        if (obj instanceof Result.Success) {
            updateUiWithUser();
            setResult(Activity.RESULT_OK);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            String errorMessage = ((Result.Error) obj).getError().getMessage();
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
