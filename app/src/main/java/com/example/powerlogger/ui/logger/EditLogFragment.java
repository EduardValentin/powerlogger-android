package com.example.powerlogger.ui.logger;

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
import android.widget.Toast;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentEditLogBindingImpl;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.ui.logger.listeners.OnEditLogListener;

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
            Log.e("PARCEABLE ERROR EDIT LOG", "Can't parse edit log.");
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
