package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.utils.APIError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class LoggerFragment extends Fragment {

    private LoggerViewModel loggerViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private ListView logsListView;
    private FloatingActionButton nextDayButton;
    private FloatingActionButton prevDayButton;
    private TextView currentDateTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        loggerViewModel = ViewModelProviders.of(this).get(LoggerViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(this.getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.fetchGroups();

        View root = inflater.inflate(R.layout.fragment_logger, container, false);

        FloatingActionButton addNewExerciceBtn = root.findViewById(R.id.addLogButton);


        logsListView = root.findViewById(R.id.logsListView);
        nextDayButton = root.findViewById(R.id.nextDayButton);
        prevDayButton = root.findViewById(R.id.prevDayButton);
        currentDateTextView = root.findViewById(R.id.currentDateTextView);

        addNewExerciceBtn.setOnClickListener(this::openAddFragment);

        Context context = this.getContext();

        loggerViewModel.getLogs().observe(this, logDTOS -> {
            LogListAdapter logListAdapter = new LogListAdapter(context, logDTOS, v -> {
                LogListItemViewHolder viewHolder = (LogListItemViewHolder) v.getTag();
                openEditFragment(loggerViewModel
                        .getLogs()
                        .getValue()
                        .get(viewHolder.getPosition()));
            });
            logsListView.setAdapter(logListAdapter);
        });

        loggerViewModel.getCurrentDateInViewLive().observe(this, date -> handleDateChange(date));

        mainActivityViewModel.getApiGroupsError().observe(this, this::onError);
        mainActivityViewModel.getApiLogsError().observe(this, this::onError);

        nextDayButton.setOnClickListener(v -> loggerViewModel.addDaysToCurrentDateInView(1));
        prevDayButton.setOnClickListener(v -> loggerViewModel.addDaysToCurrentDateInView(-1));

        return root;
    }

    public void openAddFragment(View v) {
        Fragment addLogFragment = new CreateOrEditLogFragment();

        Bundle data = new Bundle();
        data.putString(Constants.CURRENT_DATE_BUNDLE_KEY, Long.toString(loggerViewModel.getCurrentDateInViewLive().getValue().toEpochDay()));
        addLogFragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, addLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openEditFragment(LogDTO logDTO) {
        Fragment editLogFragment = new CreateOrEditLogFragment();

        Bundle data = new Bundle();
        data.putString(Constants.LOG_ID_BUNDLE_KEY, logDTO.getId().toString());

        editLogFragment.setArguments(data);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, editLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void handleDateChange(LocalDate newDate) {
        if (isSameDay(newDate, LocalDate.now())) {
            currentDateTextView.setText("Today");
        } else {
            currentDateTextView.setText(newDate.toString());
        }
        loggerViewModel.fetchLogs(newDate);
    }

    private void onError(APIError apiError) {
        Toast.makeText(this.getContext(), apiError.getUiMessage(), Toast.LENGTH_LONG);
    }

    private boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }
}