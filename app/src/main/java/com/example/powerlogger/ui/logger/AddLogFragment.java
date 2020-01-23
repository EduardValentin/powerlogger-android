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
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;

import java.util.Arrays;

public class AddLogFragment extends Fragment {
    private EditText logName;
    private EditText logIntensity;
    private Spinner logType;
    private Spinner groups;
    private Button addLogButton;
    private LoggerViewModel loggerViewModel;
    private OnFragmentInteractionListener mListener;

    public AddLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_log, container, false);

        logName = view.findViewById(R.id.logName);
        logIntensity = view.findViewById(R.id.logIntensity);
        logType = view.findViewById(R.id.logType);
        addLogButton = view.findViewById(R.id.confirmAddLog);
        groups = view.findViewById(R.id.addLogGroupSpinner);
        loggerViewModel.getGroups().observe(this, groupDTOS -> {
            ArrayAdapter<GroupDTO> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, groupDTOS);
            groups.setAdapter(arrayAdapter);
        });

        String[] logTypes = Arrays.stream(LogType.values()).map(LogType::toString).toArray(String[]::new);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, logTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logType.setAdapter(arrayAdapter);

        addLogButton.setOnClickListener(v -> onAddLogConfirm(v));
        return view;
    }

    private void handleError(Throwable t) {
        Toast.makeText(this.getContext(), "Something wrong happened", Toast.LENGTH_LONG);
    }

    private void handleSuccess() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void onAddLogConfirm(View v) {
        LogDTO toAdd = new LogDTO(
                logName.getText().toString(),
                logType.getSelectedItem().toString(),
                logIntensity.getText().toString()
        );
        loggerViewModel.addLog(toAdd, o -> handleSuccess(), t -> handleError(t));
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
}
