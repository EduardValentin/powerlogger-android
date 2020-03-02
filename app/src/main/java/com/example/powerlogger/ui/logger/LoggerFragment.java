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
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.utils.APIError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
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
        loggerViewModel =
                ViewModelProviders.of(this).get(LoggerViewModel.class);

        mainActivityViewModel = ViewModelProviders.of(this.getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.fetchLogs(new Date());
        mainActivityViewModel.fetchGroups();

        View root = inflater.inflate(R.layout.fragment_logger, container, false);

        Button addNewExerciceBtn = root.findViewById(R.id.addLogButton);
        Button selectLogsGroup = root.findViewById(R.id.includeGroupButton);
        logsListView = root.findViewById(R.id.logsListView);
        nextDayButton = root.findViewById(R.id.nextDayButton);
        prevDayButton = root.findViewById(R.id.prevDayButton);
        currentDateTextView = root.findViewById(R.id.currentDateTextView);

        addNewExerciceBtn.setOnClickListener(view -> {
            Fragment AddLogFragment = new CreateOrEditLogFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, AddLogFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        selectLogsGroup.setOnClickListener(view -> {
            Fragment AddLogFragment = new AddLogsGroupFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, AddLogFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        Context context = this.getContext();

        mainActivityViewModel.getLogsLiveData().observe(this, logDTOS -> {
            LogListAdapter logListAdapter = new LogListAdapter(context, logDTOS, v -> {
                LogListItemViewHolder viewHolder = (LogListItemViewHolder) v.getTag();
                openEditFragment(mainActivityViewModel
                                .getLogsLiveData()
                                .getValue()
                                .get(viewHolder.getPosition()));
            });
            logsListView.setAdapter(logListAdapter);
        });

        mainActivityViewModel.getCurreantDateInViewLive().observe(this, date -> handleDateChange(date));

        mainActivityViewModel.getApiGroupsError().observe(this, this::onError);
        mainActivityViewModel.getApiLogsError().observe(this, this::onError);

        nextDayButton.setOnClickListener(v -> mainActivityViewModel.addDaysToCurrentDateInView(1));
        prevDayButton.setOnClickListener(v -> mainActivityViewModel.addDaysToCurrentDateInView(-1));

        return root;
    }

    public void openEditFragment(LogDTO logDTO) {
        Fragment editLogFragment = new CreateOrEditLogFragment();

        Bundle data = new Bundle();
        data.putString("logId", logDTO.getId().toString());

        editLogFragment.setArguments(data);
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, editLogFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void handleDateChange(Date newDate) {
        if (isSameDay(newDate, new Date())) {
            currentDateTextView.setText("Today");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
            currentDateTextView.setText(dateFormat.format(newDate));
        }

        mainActivityViewModel.fetchLogs(newDate);
    }

    private void onError(APIError apiError) {
        Toast.makeText(this.getContext(), apiError.getUiMessage(), Toast.LENGTH_LONG);
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}