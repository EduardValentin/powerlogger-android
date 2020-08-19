package com.eduardv.powerlogger.ui.logger;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

//import com.example.powerlogger.AppDatabase;
import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentCreateLogBindingImpl;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.logs.IntensityType;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.utils.ArrayUtills;
import com.eduardv.powerlogger.utils.StringUtils;
import com.google.android.material.chip.Chip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CreateLogFragment extends Fragment {
    private MainActivityViewModel mainActivityViewModel;
    public CreateOrEditLogViewModel createOrEditLogViewModel;

    private String currentDateInView;

    private FragmentCreateLogBindingImpl bindingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle data = getArguments();

        try {
            currentDateInView = data.getString(LogConstants.CURRENT_DATE_BUNDLE_KEY);
        } catch (NullPointerException e) {
            currentDateInView = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        createOrEditLogViewModel = new ViewModelProvider(this).get(CreateOrEditLogViewModel.class);

        // Inflate the layout for this fragment
        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_create_log, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setModel(createOrEditLogViewModel);
        bindingFragment.setLifecycleOwner(this);

        bindingFragment.exerciseType.setOnConfirmConsumer(this::onSelectCategory);
        bindingFragment.exercisesSpinner.setOnConfirmConsumer(this::onSelectExercise);

        bindingFragment.addExerciseButton.setOnClickListener(this::onAddNewExercise);

        mainActivityViewModel.getExerciseLiveData().observe(getViewLifecycleOwner(), bindingFragment.exercisesSpinner::setItems);
        bindingFragment.exercisesSpinner.setTitle(getStringRes(R.string.pick_exercise_title));

        mainActivityViewModel.getGroupsLiveData().observe(getViewLifecycleOwner(), bindingFragment.exerciseGroup::setItems);

        bindingFragment.exerciseGroup.setOnConfirmConsumer(this::onSelectGroups);
        bindingFragment.exerciseGroup.setTitle(getStringRes(R.string.add_to_group_title));

        mainActivityViewModel.getExerciseCategoriesLive().observe(getViewLifecycleOwner(), bindingFragment.exerciseType::setItems);

        bindingFragment.exerciseType.setTitle(getStringRes(R.string.exercise_type_title));

        bindingFragment.logIntensityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = (String) parent.getItemAtPosition(position);
                IntensityType intensityType = IntensityType.valueOf(itemSelected.toUpperCase());
                createOrEditLogViewModel.getLogDTO().setIntensityType(intensityType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handleUIForAddMode();

        return view;
    }

    private void handleError(Throwable t) {
        Toast.makeText(this.getActivity().getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
    }

    private void handleSuccess(Object o) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void handleUIForAddMode() {
        bindingFragment.confirmAddLog.setOnClickListener(this::onAddLogConfirm);
    }

    private void onAddLogConfirm(View v) {
        boolean hasErrors = hasErrors();

        if (hasErrors) {
            return;
        }

        createOrEditLogViewModel.getLogDTO().setCreatedAt(currentDateInView);

        if (createOrEditLogViewModel.isCreatingNewExercise()) {
            // create exercise here and get the response
            createOrEditLogViewModel.createExercise(exercise -> {
                createOrEditLogViewModel.addLog(exercise, this::handleSuccess);
            }, this::handleError);
        } else {
            createOrEditLogViewModel.addLog(this::handleSuccess);
        }
    }

    public void onAddNewExercise(View v) {
        if (bindingFragment.addNewExerciseLayout.isExpanded()) {
            createOrEditLogViewModel.setCreatingNewExercise(false);
            bindingFragment.exercisesSpinnerLayout.setVisibility(View.VISIBLE);
        } else {
            createOrEditLogViewModel.setCreatingNewExercise(true);
            bindingFragment.exercisesSpinnerLayout.setVisibility(View.INVISIBLE);
        }

        bindingFragment.addNewExerciseLayout.toggle();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSelectGroups(Object groups) {
        List<GroupDTO> selectedGroups = (List<GroupDTO>) groups;

        createOrEditLogViewModel.getCreatingExercise().setGroupIds(
                selectedGroups.stream().map(GroupDTO::getId).collect(Collectors.toList()));

        bindingFragment.chipGroup.removeAllViews();

        selectedGroups.forEach(checkedGroup -> {

            Chip chip = new Chip(getContext());
            chip.setText(checkedGroup.getName());
            chip.setCloseIconVisible(true);

            chip.setOnCloseIconClickListener(v -> {
                int index = ArrayUtills.findIndexByPredicate(
                        mainActivityViewModel.getGroupsLiveData().getValue(),
                        checkedGroup::equalIds
                );

                bindingFragment.exerciseGroup.setSelectedAtIndex(index, false);

                createOrEditLogViewModel.getCreatingExercise()
                        .getGroupIds()
                        .removeIf(checkedGroup.getId()::equals);

                bindingFragment.chipGroup.removeView(v);
            });

            bindingFragment.chipGroup.addView(chip);
        });
    }

    public void onSelectCategory(Object category) {
        try {
            ExerciseCategoryDTO cat = (ExerciseCategoryDTO) category;
            createOrEditLogViewModel.getCreatingExercise().setCategory(cat);
            updateDurationType(cat);
        } catch (Exception ex) {
            Log.e("CATEGORY SELECT ERR:", ex.getMessage());
        }
    }

    public void onSelectExercise(Object exerciseDTO) {
        try {
            ExerciseDTO casted = (ExerciseDTO) exerciseDTO;
            createOrEditLogViewModel.setSelectedExercise(casted);
        } catch (Exception ex) {
            Log.e("EXERCISE SELECT ERR:", ex.getMessage());
        }
    }

    private void updateDurationType(ExerciseCategoryDTO category) {
        String[] values = (category != null && category.getIntensityFactor() == null)
                ? new String[]{"Minutes"}
                : new String[]{"Minutes", "Reps"};

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bindingFragment.logIntensityType.setAdapter(adapter);
    }

    private boolean hasErrors() {
        boolean hasErrors = false;

        if (createOrEditLogViewModel.isCreatingNewExercise()) {
            CreateExerciseRequestDTO creating = createOrEditLogViewModel.getCreatingExercise();
            if (StringUtils.isEmpty(creating.getName())) {
                bindingFragment.exerciseNameLayout.setError("Exercise name is required");
                hasErrors = true;
            } else {
                bindingFragment.exerciseNameLayout.setError(null);
            }

            if (creating.getCategory() == null) {
                bindingFragment.exerciseTypeLayout.setError("Exercise type is required");
                hasErrors = true;
            } else {
                bindingFragment.exerciseTypeLayout.setError(null);
            }

        } else {
            if (createOrEditLogViewModel.getSelectedExercise() == null) {
                bindingFragment.exercisesSpinnerLayout.setError("Exercise is required");
                hasErrors = true;
            } else {
                bindingFragment.exercisesSpinnerLayout.setError(null);
            }
        }

        LogDTO log = createOrEditLogViewModel.getLogDTO();

        if (log.getIntensity() == 0) {
            bindingFragment.logIntensityLayout.setError("Value cannot be 0");
            hasErrors = true;
        } else {
            bindingFragment.logIntensityLayout.setError(null);
        }


        return hasErrors;
    }

    private String getStringRes(int res) {
        return getResources().getString(res);
    }
}
