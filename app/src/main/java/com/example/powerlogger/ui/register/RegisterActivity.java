package com.example.powerlogger.ui.register;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.R;

import java.time.LocalDate;

public class RegisterActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
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

}
