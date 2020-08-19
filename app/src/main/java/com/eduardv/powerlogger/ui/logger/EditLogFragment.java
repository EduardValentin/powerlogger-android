package com.eduardv.powerlogger.ui.logger;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentEditLogBindingImpl;
import com.eduardv.powerlogger.dto.logs.IntensityType;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.ui.logger.listeners.OnEditLogListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditLogFragment extends Fragment {

    private EditLogViewModel editLogViewModel;
    private LogDTO logToEdit;
    private FragmentEditLogBindingImpl bindingFragment;
    private OnEditLogListener listener;

    private String actionButtonText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (OnEditLogListener) context;
    }

    public EditLogFragment(String actionButtonText) {
        this.actionButtonText = actionButtonText;
    }

    public EditLogFragment() {
        this.actionButtonText = "SAVE";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle data = getArguments();

        try {
            logToEdit = data.getParcelable(LogConstants.LOG_BUNDLE_KEY);
        } catch (NullPointerException e) {
            Log.e("ERROR EDIT LOG", "Can't parse edit log.");
            getActivity().getSupportFragmentManager().popBackStack();
        }

        editLogViewModel = ViewModelProviders.of(this).get(EditLogViewModel.class);
        editLogViewModel.setLogToEdit(logToEdit);

        // Inflate the layout for this fragment
        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_log, container, false);

        View view = bindingFragment.getRoot();

        bindingFragment.setModel(editLogViewModel);
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        String[] intensityTypes = getResources().getStringArray(R.array.intensityTypeEntries);

        if (logToEdit.getExercise().getCategory().getIntensityFactor() == null) {
            intensityTypes = new String[]{"Minutes"};
        }

        int selectedIndex;
        for (selectedIndex = 0; selectedIndex < intensityTypes.length; selectedIndex++) {
            IntensityType current = IntensityType.valueOf(intensityTypes[selectedIndex].toUpperCase());
            if (current == logToEdit.getIntensityType()) {
                break;
            }
        }


        ArrayAdapter<String> adapter =new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, intensityTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bindingFragment.logIntensityType.setAdapter(adapter);


        bindingFragment.logIntensityType.setSelection(selectedIndex);

        bindingFragment.logIntensityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elem = (String) parent.getItemAtPosition(position);
                editLogViewModel.getLogToEdit().setIntensityType(IntensityType.valueOf(elem.toUpperCase()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bindingFragment.confirmEditLog.setText(actionButtonText);
        return view;
    }

    public void onSave(View v) {
        listener.onEditLog(editLogViewModel.getLogToEdit(), this::handleSuccess, this::handleError);
    }

    private void handleError(Throwable t) {
        Log.i("EDIT LOG", "Error while editing log");
        Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(Object o) {
        Log.i("EDIT LOG", "Success");
        getActivity().getSupportFragmentManager().popBackStack();
    }

}
