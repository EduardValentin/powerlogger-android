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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.utils.ArrayUtills;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateOrEditLogFragment extends Fragment {
    private EditText logName;
    private EditText logIntensity;
    private Spinner logType;
    private Spinner groupsSpinner;
    private Button addLogButton;
    private LoggerViewModel loggerViewModel;
    private OnFragmentInteractionListener mListener;
    private String editLogId;
    private String currentDateInView;
    private TextView logNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle data = getArguments();
        try {
            editLogId = data.getString("logId");
        } catch (NullPointerException e) {
            editLogId = null;
        }


        try {
            currentDateInView = data.getString("currentDateInView");
        } catch (NullPointerException e) {
            currentDateInView = null;
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_log, container, false);

        logName = view.findViewById(R.id.logName);
        logIntensity = view.findViewById(R.id.logIntensity);
        logType = view.findViewById(R.id.logType);
        addLogButton = view.findViewById(R.id.confirmAddLog);
        groupsSpinner = view.findViewById(R.id.addLogGroupSpinner);
        logNotes = view.findViewById(R.id.logNotes);

        loggerViewModel.getGroups().observe(this, groupDTOS -> {
            ArrayAdapter<GroupDTO> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, groupDTOS);
            groupsSpinner.setAdapter(arrayAdapter);
        });

        String[] logTypes = Arrays.stream(LogType.values()).map(LogType::toString).toArray(String[]::new);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, logTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logType.setAdapter(arrayAdapter);

        if (editLogId != null) {
          handleUIForEditMode();
        } else {
           handleUIForAddMode();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        loggerViewModel = ViewModelProviders.of(this).get(LoggerViewModel.class);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void handleError(Throwable t) {
        Toast.makeText(this.getContext(), "Something wrong happened", Toast.LENGTH_LONG);
    }

    private void handleSuccess() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void handleUIForEditMode() {
        addLogButton.setText("Edit Log");
        addLogButton.setOnClickListener(v -> onEditLogConfirm(v));

        List<GroupDTO> groups = loggerViewModel.getGroups().getValue();
        LogDTO currentLogToEdit = loggerViewModel
                .getLogs()
                .getValue()
                .stream()
                .filter(log -> log.getId().equalsIgnoreCase(editLogId))
                .findFirst()
                .orElse(null);

        if (currentLogToEdit != null) {
            logName.setText(currentLogToEdit.getName());
            logType.setSelection(LogType.valueOf(currentLogToEdit.getType()).ordinal());
            logIntensity.setText(Integer.toString(
                    currentLogToEdit.getMinutes())
            );
            logNotes.setText(currentLogToEdit.getNotes());

            GroupDTO groupOfLog = groups
                    .stream()
                    .filter(group -> group.getId().equalsIgnoreCase(currentLogToEdit.getGroupId()))
                    .findFirst()
                    .orElse(null);

            if (groupOfLog != null) {
                groupsSpinner.setSelection(ArrayUtills.findIndexInArray(groups, groupOfLog));
            }

            logNotes.setText(currentLogToEdit.getNotes());
        }
    }

    private void handleUIForAddMode() {
        addLogButton.setText("Add Log");
        addLogButton.setOnClickListener(v -> onAddLogConfirm(v));
    }

    private LogDTO constructLogFromInputs () {

        GroupDTO selectedGroup = (GroupDTO) groupsSpinner.getSelectedItem();

        GroupDTO groupDTO = loggerViewModel
                .getGroups()
                .getValue()
                .stream()
                .filter(grp -> grp.getName().equalsIgnoreCase(selectedGroup.getName()))
                .findAny()
                .orElse(null);

        LogDTO computedLog = new LogDTO(
                logName.getText().toString(),
                logType.getSelectedItem().toString(),
                Integer.parseInt(logIntensity.getText().toString()),
                logNotes.getText().toString(),
                currentDateInView
        );

        if (groupDTO != null) {
            computedLog.setGroupId(groupDTO.getId());
        }

        return computedLog;
    }

    private void onAddLogConfirm(View v) {
        LogDTO toAdd = constructLogFromInputs();

        loggerViewModel.addLog(toAdd, o -> handleSuccess(), t -> handleError(t));
    }

    private void onEditLogConfirm(View v) {
        LogDTO toEdit = constructLogFromInputs();
        toEdit.setId(editLogId);
        loggerViewModel.updateLog(toEdit, o -> handleSuccess(), t -> handleError(t));
    }

}
