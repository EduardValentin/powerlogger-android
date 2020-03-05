package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.powerlogger.MainActivity;
import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.ExerciseCategory;
import com.example.powerlogger.utils.ArrayUtills;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CreateOrEditLogFragment extends Fragment {
    private EditText exerciseName;
    private Spinner exerciseType;
    private Button addNewExerciseButton;
    private Spinner exercisesSpinner;

    private LinearLayout exerciseControlsLayout;
    private ExpandableLayout addNewExerciseLayout;

    private EditText logIntensity;
    private TextView logNotes;
    private Spinner groupsSpinner;

    private Button addLogButton;

    private MainActivityViewModel mainActivityViewModel;
    private CreateOrEditLogViewModel createOrEditLogViewModel;

    private String editLogId;
    private LocalDate currentDateInView;

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_log, container, false);

        addNewExerciseLayout = view.findViewById(R.id.addNewExerciseLayout);
        exerciseControlsLayout = view.findViewById(R.id.exerciseControls);

        mainActivityViewModel = ViewModelProviders.of(this.getActivity()).get(MainActivityViewModel.class);
        createOrEditLogViewModel = ViewModelProviders.of(this).get(CreateOrEditLogViewModel.class);

        groupsSpinner = view.findViewById(R.id.exerciseGroup);
        exercisesSpinner = view.findViewById(R.id.exercisesSpinner);

        addNewExerciseButton = view.findViewById(R.id.addExerciseButton);

        addNewExerciseButton.setOnClickListener(this::onAddNewExercise);

        mainActivityViewModel.getExerciseLiveData().observe(this, exerciseDTOS -> {
            List<ExerciseDTO> exercises = new ArrayList<>();
            exercises.addAll(exerciseDTOS);
            ExerciseDTO placeholder = new ExerciseDTO();
            placeholder.setName("Select exercise");
            exercises.add(placeholder);
            ArrayAdapter<ExerciseDTO> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, exercises);
            exercisesSpinner.setAdapter(arrayAdapter);
        });

        mainActivityViewModel.getGroupsLiveData().observe(this, groupDTOS -> {
            List<GroupDTO> groupsList = new ArrayList<>();
            groupsList.addAll(groupDTOS);
            GroupDTO placeholder = new GroupDTO();
            placeholder.setName("Select groups");
            groupsList.add(0, placeholder);
            ArrayAdapter<GroupDTO> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, groupsList);
            groupsSpinner.setAdapter(arrayAdapter);
        });


        exerciseName = view.findViewById(R.id.exerciseName);
        logIntensity = view.findViewById(R.id.logIntensity);
        exerciseType = view.findViewById(R.id.exerciseType);
        addLogButton = view.findViewById(R.id.confirmAddLog);
        logNotes = view.findViewById(R.id.logNotes);

        List<String> exerciseCategories =
                Arrays.stream(ExerciseCategory.values())
                        .map(ExerciseCategory::getName)
                        .collect(Collectors.toList());

        exerciseCategories.add(0, "Exercise category");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                exerciseCategories);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseType.setAdapter(arrayAdapter);

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
        addLogButton.setText("Edit Log");
        addLogButton.setOnClickListener(this::onEditLogConfirm);
        List<GroupDTO> groups = mainActivityViewModel.getGroupsLiveData().getValue();

//        LogDTO currentLogToEdit = loggerViewModel
//                .getLogs()
//                .getValue()
//                .stream()
//                .filter(log -> log.getId().equals(editLogId))
//                .findFirst()
//                .orElse(null);
//
//        if (currentLogToEdit != null) {
//            logName.setText(currentLogToEdit.getName());
//            logType.setSelection(LogType.valueOf(currentLogToEdit.getType()).ordinal());
//            logIntensity.setText(Integer.toString(
//                    currentLogToEdit.getMinutes())
//            );
//            logNotes.setText(currentLogToEdit.getNotes());
//
//            GroupDTO groupOfLog = groups
//                    .stream()
//                    .filter(group -> group.getId().equalsIgnoreCase(currentLogToEdit.getGroupId()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (groupOfLog != null) {
//                groupsSpinner.setSelection(ArrayUtills.findIndexInArray(groups, groupOfLog));
//            }
//
//            logNotes.setText(currentLogToEdit.getNotes());
//        }
    }

    private void handleUIForAddMode() {
//        addLogButton.setText("Add Log");
//        addLogButton.setOnClickListener(v -> onAddLogConfirm(v));
    }

//    private LogDTO constructLogFromInputs () {

//        GroupDTO selectedGroup = (GroupDTO) groupsSpinner.getSelectedItem();
//
//        GroupDTO groupDTO = loggerViewModel
//                .getGroups()
//                .getValue()
//                .stream()
//                .filter(grp -> grp.getName().equalsIgnoreCase(selectedGroup.getName()))
//                .findAny()
//                .orElse(null);
//
//
//        LogDTO computedLog = new LogDTO(
//                logName.getText().toString(),
//                logType.getSelectedItem().toString(),
//                Integer.parseInt(logIntensity.getText().toString()),
//                logNotes.getText().toString(),
//                new SimpleDateFormat("yyyy-MM-dd").format(mainActivityViewModel.getCurreantDateInViewLive().getValue())
//        );
//
//        if (groupDTO != null) {
//            computedLog.setGroupId(groupDTO.getId());
//        }
//
//        return computedLog;
//    }

    private void onAddLogConfirm(View v) {
//        LogDTO toAdd = constructLogFromInputs();
//
//        loggerViewModel.addLog(toAdd, o -> handleSuccess(), t -> handleError(t));
    }

    private void onEditLogConfirm(View v) {
//        LogDTO toEdit = constructLogFromInputs();
//        toEdit.setyId(editLogId);
//        loggerViewModel.updateLog(toEdit, o -> handleSuccess(), t -> handleError(t));
    }

    public void onAddNewExercise(View v) {
        if (addNewExerciseLayout.isExpanded()) {
            createOrEditLogViewModel.setCreatingNewExercise(false);
        } else {
            createOrEditLogViewModel.setCreatingNewExercise(true);
        }

        addNewExerciseLayout.toggle();
    }
}
