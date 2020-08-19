package com.eduardv.powerlogger.ui.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.ProfileFragmentBindingImpl;
import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.dto.user.UserDTO;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private ProfileFragmentBindingImpl binding;

    private static boolean stringNotEmpty(String errorValue) {
        return errorValue != null && errorValue.length() > 0;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        binding.setView(this);
        binding.setViewmodel(viewModel);

        viewModel.updateDtoWithCurrentProfile();
        viewModel.getErrorsLiveData().observe(getViewLifecycleOwner(), this::errorsChanged);
        binding.userBirthDate.setOnClickListener(this::openDatePicker);

        UserSettingsDTO settings = viewModel.getDto().getSettings();
        binding.userWeightLayout.setHint("Weight (" + settings.getWeightUnit().getShortname() + " )");
        binding.userHeightLayout.setHint("Height (" + settings.getHeightUnit().getShortname() + ")");

        return binding.getRoot();
    }

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    private void openDatePicker(View view) {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -5);
        long past5Years = calendar.getTimeInMillis();


        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -100);
        long past100years = calendar.getTimeInMillis();

        String birthDate = viewModel.getDto().getSettings().getBirthDate();
        LocalDate selected = LocalDate.parse(birthDate);

        long selectedMilis = selected.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setEnd(past5Years)
                .setOpenAt(selectedMilis)
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

    public void onConfirm() {
        viewModel.sendProfileUpdates(this::onSuccess, this::onFail);
    }

    private void onSuccess(UserDTO o) {
        getParentFragmentManager().popBackStack();
    }

    private void onFail(Throwable t) {
        getParentFragmentManager().popBackStack();
        Toast.makeText(getContext(), R.string.something_wrong_happened, Toast.LENGTH_SHORT).show();
    }

    private void errorsChanged(Map<String, String> errorsMap) {
        binding.registerUsernameLayout.setError(errorsMap.get("username"));
        binding.registerEmailLayout.setError(errorsMap.get("email"));
        binding.userWeightLayout.setError(errorsMap.get("weight"));
        binding.userHeightLayout.setError(errorsMap.get("height"));
        binding.userBirthDateLayout.setError(errorsMap.get("birthDate"));

        // Check if data valid
        enableButtonIfFieldsOk();
    }

    private void enableButtonIfFieldsOk() {
        boolean allNull = viewModel.getErrorsLiveData().getValue()
                .values()
                .stream().noneMatch(ProfileFragment::stringNotEmpty);

        binding.saveProfileData.setEnabled(allNull);
    }
}

