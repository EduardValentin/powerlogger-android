package com.example.powerlogger.ui.logger;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentCreateEditLogBinding;
import com.example.powerlogger.databinding.FragmentRegisterBinding;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.model.ExerciseCategory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateOrEditLogFragment extends Fragment {
    private MainActivityViewModel mainActivityViewModel;
    public CreateOrEditLogViewModel createOrEditLogViewModel;

    private String editLogId;
    private LocalDate currentDateInView;

    private final AdapterView.OnItemSelectedListener onCategorySelectListener = new CustomItemSelectedListener<>(this::onSelectCategory);
    private final AdapterView.OnItemSelectedListener onExerciseSelectListener = new CustomItemSelectedListener<>(this::onSelectExercise);

    private FragmentCreateEditLogBinding bindingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle data = getArguments();
        try {
            editLogId = data.getString(Constants.LOG_ID_BUNDLE_KEY);
        } catch (NullPointerException e) {
            editLogId = null;
        }

        try {
            String dateMiliStr = data.getString(Constants.CURRENT_DATE_BUNDLE_KEY);
            currentDateInView = Instant.ofEpochMilli(Long.parseLong(dateMiliStr)).atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (NullPointerException e) {
            currentDateInView = null;
        }

        mainActivityViewModel = ViewModelProviders.of(this.getActivity()).get(MainActivityViewModel.class);
        createOrEditLogViewModel = ViewModelProviders.of(this).get(CreateOrEditLogViewModel.class);

        // Inflate the layout for this fragment
        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_create_edit_log, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setModel(createOrEditLogViewModel);
        bindingFragment.setLifecycleOwner(this);
        bindingFragment.exerciseGroup.setPlaceholderText("Select groups");
        bindingFragment.exerciseType.setOnItemSelectedListener(this.onCategorySelectListener);
        bindingFragment.exercisesSpinner.setOnItemSelectedListener(this.onExerciseSelectListener);
        bindingFragment.addExerciseButton.setOnClickListener(this::onAddNewExercise);

        mainActivityViewModel.getExerciseLiveData().observe(this, exerciseDTOS -> {
            List<ExerciseDTO> exercises = new ArrayList<>();
            exercises.addAll(exerciseDTOS);
            ExerciseDTO placeholder = new ExerciseDTO();
            placeholder.setName("Select exercise");
            exercises.add(placeholder);

            ArrayAdapter<ExerciseDTO> arrayAdapter = new ArrayAdapter<>(this.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, exercises);
            bindingFragment.exercisesSpinner.setAdapter(arrayAdapter);
        });

        mainActivityViewModel.getGroupsLiveData().observe(this, groupDTOS -> {
            List<GroupDTO> groupsList = new ArrayList<>();
            groupsList.addAll(groupDTOS);
            GroupDTO placeholder = new GroupDTO();
            placeholder.setName("Select groups");
            groupsList.add(0, placeholder);
            bindingFragment.exerciseGroup.setItems(groupsList);
        });

        List<String> exerciseCategories =
                Arrays.stream(ExerciseCategory.values())
                        .map(ExerciseCategory::getName)
                        .collect(Collectors.toList());

        exerciseCategories.add(0, "Type");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                exerciseCategories);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bindingFragment.exerciseType.setAdapter(arrayAdapter);

        if (editLogId != null) {
            handleUIForEditMode();
        } else {
            handleUIForAddMode();
        }

        return view;
    }

    private void handleError(Throwable t) {
        Toast.makeText(this.getActivity().getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
    }

    private void handleSuccess() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void handleUIForEditMode() {
        bindingFragment.confirmAddLog.setOnClickListener(this::onEditLogConfirm);
        List<GroupDTO> groups = mainActivityViewModel.getGroupsLiveData().getValue();
    }

    private void handleUIForAddMode() {
        bindingFragment.confirmAddLog.setOnClickListener(this::onAddLogConfirm);
    }

    private void onAddLogConfirm(View v) {
        if (createOrEditLogViewModel.isCreatingNewExercise()) {
            // create exercise here and get the response
            createOrEditLogViewModel.createExercise(exercise -> {
                        createOrEditLogViewModel.addLog(exercise);
                        handleSuccess();
                    },
                    this::handleError);
        } else {
            createOrEditLogViewModel.addLog();
        }
    }

    private void onEditLogConfirm(View v) {

    }

    public void onAddNewExercise(View v) {
        if (bindingFragment.addNewExerciseLayout.isExpanded()) {
            createOrEditLogViewModel.setCreatingNewExercise(false);
        } else {
            createOrEditLogViewModel.setCreatingNewExercise(true);
        }

        bindingFragment.addNewExerciseLayout.toggle();
    }

    public void onSelectCategory(String category) {
        try {
            createOrEditLogViewModel.getCreatingExercise().setCategory(ExerciseCategory.fromString(category));
        } catch (Exception ex) {
            Log.e("CATEGORY SELECT ERR:", ex.getMessage());
        }
    }

    public void onSelectExercise(ExerciseDTO exerciseDTO) {
        try {
            createOrEditLogViewModel.setSelectedExercise(exerciseDTO);
        } catch (Exception ex) {
            Log.e("EXERCISE SELECT ERR:", ex.getMessage());
        }
    }
}
