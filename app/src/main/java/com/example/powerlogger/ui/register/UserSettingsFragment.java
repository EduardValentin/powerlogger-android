package com.example.powerlogger.ui.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.powerlogger.MainActivity;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentRegisterBinding;
import com.example.powerlogger.databinding.FragmentUserSettingsBinding;
import com.example.powerlogger.databinding.FragmentUserSettingsBindingImpl;
import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.ui.DatePickerFragment;
import com.example.powerlogger.utils.Result;
import com.example.powerlogger.utils.StringUtils;

import java.time.LocalDate;

public class UserSettingsFragment extends Fragment {

    public UserSettingsFragment() {
    }

    private RegisterViewModel registerViewModel;
    private ProgressBar loadingProgressBarr;

    private EditText dateTextInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        FragmentUserSettingsBinding fragment = DataBindingUtil.inflate(inflater, R.layout.fragment_user_settings, container, false);
        View view = fragment.getRoot();
        fragment.setViewmodel(registerViewModel);
        fragment.setLifecycleOwner(this);

        final Button registerButton = view.findViewById(R.id.registerButton);
        loadingProgressBarr = view.findViewById(R.id.loadingSpinner);

        registerButton.setOnClickListener(this::registerAction);
        dateTextInput = view.findViewById(R.id.userBirthDate);
        dateTextInput.setOnClickListener(this::openDatePicker);

        final EditText birthDate = view.findViewById(R.id.userBirthDate);
        final EditText height = view.findViewById(R.id.userHeight);
        final EditText weight = view.findViewById(R.id.userWeight);
        final Button registerBtn = view.findViewById(R.id.registerButton);

        registerViewModel.getSettingsFormErrors().observe(this, errors -> {
            if (!StringUtils.isEmpty(weight.getText().toString()) && errors.get(UserConstants.USER_WEIGHT_ERR) != null) {
                weight.setError(errors.get(UserConstants.USER_WEIGHT_ERR));
            }

            if (!StringUtils.isEmpty(height.getText().toString()) && errors.get(UserConstants.USER_HEIGHT_ERR) != null) {
                height.setError(errors.get(UserConstants.USER_HEIGHT_ERR));
            }

            if (!StringUtils.isEmpty(birthDate.getText().toString()) && errors.get(UserConstants.USER_BIRTH_DATE_ERR) != null) {
                birthDate.setError(errors.get(UserConstants.USER_BIRTH_DATE_ERR));
            }

            if (registerViewModel.isUserSettingsDataValid()) {
                registerBtn.setEnabled(true);
            } else {
                registerBtn.setEnabled(false);
            }
        });




        return view;
    }

    public void registerAction(View v) {
        registerViewModel.signup(obj -> {
            loadingProgressBarr.setVisibility(View.INVISIBLE);

            if (obj instanceof Result.Success) {
                updateUiWithUser();
                getActivity().setResult(Activity.RESULT_OK);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                String errorMessage = ((Result.Error) obj).getError().getMessage();
                showRegisterFailed(errorMessage);
            }
        });
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(getActivity().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(String errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openDatePicker(View v) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "date picker");
    }
}
