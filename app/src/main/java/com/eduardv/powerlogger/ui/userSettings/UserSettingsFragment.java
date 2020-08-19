package com.eduardv.powerlogger.ui.userSettings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentUserSettingsBinding;
import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.ui.register.UserConstants;
import com.eduardv.powerlogger.utils.StringUtils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.eduardv.powerlogger.dto.units.HeightUnit.CENTIMETERS;
import static com.eduardv.powerlogger.dto.units.WeightUnit.KILOGRAM;

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

        List<HeightUnit> heightUnits = Arrays.asList(HeightUnit.values());
        binding.heightUnit.setItems(heightUnits);
        binding.heightUnit.setSelectedItem(heightUnits.indexOf(CENTIMETERS));
        binding.heightUnit.setOnConfirmConsumer(userSettingsViewModel.getUserSettingsDTO()::setHeightUnit);

        List<WeightUnit> weightValues = Arrays.asList(WeightUnit.values());
        binding.weightUnit.setItems(weightValues);
        binding.weightUnit.setSelectedItem(weightValues.indexOf(KILOGRAM));
        binding.weightUnit.setOnConfirmConsumer(userSettingsViewModel.getUserSettingsDTO()::setWeightUnit);

        List<Gender> genders = Arrays.asList(Gender.values());
        binding.gender.setItems(genders);
        binding.gender.setSelectedItem(0);
        binding.gender.setOnConfirmConsumer(userSettingsViewModel.getUserSettingsDTO()::setGender);

        userSettingsViewModel.getSettingsFormErrors().observe(getViewLifecycleOwner(), errors -> {
            if (!StringUtils.isEmpty(binding.userWeight.getText().toString()) && errors.get(UserConstants.USER_WEIGHT_ERR) != null) {
                binding.userWeightLayout.setError(errors.get(UserConstants.USER_WEIGHT_ERR));
            } else {
                binding.userWeightLayout.setError(null);
            }

            if (!StringUtils.isEmpty(binding.userHeight.getText().toString()) && errors.get(UserConstants.USER_HEIGHT_ERR) != null) {
                binding.userHeightLayout.setError(errors.get(UserConstants.USER_HEIGHT_ERR));
            } else {
                binding.userHeightLayout.setError(null);
            }

            if (!StringUtils.isEmpty(binding.userBirthDate.getText().toString()) && errors.get(UserConstants.USER_BIRTH_DATE_ERR) != null) {
                binding.userBirthDateLayout.setError(errors.get(UserConstants.USER_BIRTH_DATE_ERR));
            } else {
                binding.userBirthDateLayout.setError(null);
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

    private void openDatePicker(View v) {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -5);
        long past5Years = calendar.getTimeInMillis();


        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -100);
        long past100years = calendar.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setEnd(past5Years)
                .setStart(past100years)
                .build();


        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick your birth date")
                .setCalendarConstraints(constraints)
                .build();

        datePicker.addOnPositiveButtonClickListener(picked -> {
            Long epoch = (Long) picked;
            Date pickedDate = new Date(epoch);
            LocalDate ld = pickedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            binding.userBirthDate.setText(ld.format(DateTimeFormatter.ISO_DATE));
        });

        datePicker.showNow(getChildFragmentManager(), "date_picker");
    }

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    private void onSignUpResponse() {
        binding.loadingSpinner.setVisibility(View.INVISIBLE);
    }
}
