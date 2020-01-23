package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoggerFragment extends Fragment {

    private LoggerViewModel loggerViewModel;
    private ListView logsListView;
    private FloatingActionButton nextDayButton;
    private FloatingActionButton prevDayButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loggerViewModel =
                ViewModelProviders.of(this).get(LoggerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logger, container, false);

        AppCompatImageButton addNewExerciceBtn = root.findViewById(R.id.addLogButton);
        logsListView = root.findViewById(R.id.logsListView);
        nextDayButton = root.findViewById(R.id.nextDayButton);
        prevDayButton = root.findViewById(R.id.prevDayButton);

        addNewExerciceBtn.setOnClickListener(view -> {
            Fragment AddLogFragment = new AddLogFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, AddLogFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        Context context = this.getContext();
        Fragment self = this;

        loggerViewModel.getLogs().observe(this, logDTOS -> {
            ArrayAdapter<LogDTO> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, logDTOS);
            logsListView.setAdapter(arrayAdapter);
        });
        return root;
    }
}