package com.example.powerlogger.ui.units;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.UnitsFragmentBindingImpl;
import com.example.powerlogger.dto.units.HeightUnit;
import com.example.powerlogger.dto.units.WeightUnit;
import com.example.powerlogger.dto.user.UserSettingsDTO;

public class UnitsFragment extends Fragment {

    private UnitsViewModel viewModel;
    private UnitsFragmentBindingImpl binding;

    public static UnitsFragment newInstance() {
        return new UnitsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(UnitsViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.units_fragment, container, false);
        binding.setView(this);
        binding.setViewmodel(viewModel);

        binding.weightRadioGroup.setOnCheckedChangeListener(this::onWeightCheckedChanged);
        binding.heightRadioGroup.setOnCheckedChangeListener(this::onHeightCheckedChanged);

        UserSettingsDTO settingsDTO = viewModel.getUserSettings();

        if (settingsDTO.getHeightUnit() == HeightUnit.CENTIMETERS) {
            binding.centimetersButton.setChecked(true);
        } else {
            binding.feetInchesButton.setChecked(true);
        }

        if (settingsDTO.getWeightUnit() == WeightUnit.KILOGRAM) {
            binding.kiloButton.setChecked(true);
        } else {
            binding.poundButton.setChecked(true);
        }

        return binding.getRoot();
    }

    public void onConfirm() {
        viewModel.sendUnitsUpdate(this::onSuccess, this::onFail);
    }

    private void onHeightCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.centimetersButton:
                viewModel.getDto().setHeightUnit(HeightUnit.CENTIMETERS);
                break;
            case R.id.feetInchesButton:
                viewModel.getDto().setHeightUnit(HeightUnit.INCH);
                break;
        }
    }

    private void onWeightCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.kiloButton:
                viewModel.getDto().setWeightUnit(WeightUnit.KILOGRAM);
                break;
            case R.id.poundButton:
                viewModel.getDto().setWeightUnit(WeightUnit.POUND);
                break;
        }

    }

    private void onFail(Throwable t) {
        getParentFragmentManager().popBackStack();
        Toast.makeText(getContext(), R.string.something_wrong_happened, Toast.LENGTH_SHORT).show();
    }

    private void onSuccess(UserSettingsDTO o) {
        getParentFragmentManager().popBackStack();
    }
}
