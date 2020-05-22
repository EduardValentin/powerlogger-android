package com.example.powerlogger.ui.logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentLoggerBindingImpl;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.ui.includeWorkouts.IncludeGroupsActivity;
import com.example.powerlogger.utils.APIError;
import com.example.powerlogger.utils.Constants;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.transition.MaterialContainerTransform;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggerFragment extends Fragment {

    private LoggerViewModel loggerViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private FragmentLoggerBindingImpl bindingFragment;
    private LogsListFragment logsListFragment;

    public static final int INCLUDE_GROUPS_REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_logger, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        loggerViewModel = new ViewModelProvider(this).get(LoggerViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this.getActivity()).get(MainActivityViewModel.class);

        bindingFragment.addLogButton.setOnClickListener(this::openAddFragment);

        logsListFragment = new LogsListFragment();
        attachListFragment();

        loggerViewModel.getLogs().observe(getViewLifecycleOwner(), this::onChanged);
        loggerViewModel.getCurrentDateInViewLive().observe(getViewLifecycleOwner(), this::handleDateChange);

        updateTotalKcal(loggerViewModel.getLogs().getValue());

        mainActivityViewModel.getApiGroupsError().observe(getViewLifecycleOwner(), this::onError);
        mainActivityViewModel.getApiLogsError().observe(getViewLifecycleOwner(), this::onError);

        bindingFragment.nextDayButton.setOnClickListener(v -> loggerViewModel.addDaysToCurrentDateInView(1));
        bindingFragment.prevDayButton.setOnClickListener(v -> loggerViewModel.addDaysToCurrentDateInView(-1));

        bindingFragment.currentDateTextView.setOnClickListener(this::openDatePicker);
        return view;
    }

    public void openAddFragment(View v) {
        Fragment addLogFragment = new CreateLogFragment();
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setDuration(400);
        addLogFragment.setSharedElementEnterTransition(transform);

        Bundle data = new Bundle();
        data.putString(LogConstants.CURRENT_DATE_BUNDLE_KEY, loggerViewModel.getCurrentDateInViewLive().getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        addLogFragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.addSharedElement(bindingFragment.addLogButton, "shared_elem_container_logger");
        fragmentTransaction.replace(R.id.nav_host_fragment, addLogFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void onAddGroupsClick() {
        Intent intent = new Intent(getActivity(), IncludeGroupsActivity.class);
        ArrayList<GroupDTO> groups = (ArrayList<GroupDTO>) mainActivityViewModel.getGroupsLiveData().getValue();
        ArrayList<ExerciseDTO> exercises = (ArrayList<ExerciseDTO>) mainActivityViewModel.getExerciseLiveData().getValue();

        intent.putParcelableArrayListExtra(Constants.GROUPS, new ArrayList<>(groups));
        intent.putParcelableArrayListExtra(Constants.EXERCISES, new ArrayList<>(exercises));
        intent.putExtra(Constants.DATE, loggerViewModel.getCurrentDateInViewLive().getValue());

        getActivity().startActivityForResult(intent, INCLUDE_GROUPS_REQUEST_CODE);
    }

    private void handleDateChange(LocalDate newDate) {
        if (isSameDay(newDate, LocalDate.now())) {
            bindingFragment.currentDateTextView.setText("Today");
        } else {
            bindingFragment.currentDateTextView.setText(newDate.toString());
        }
        loggerViewModel.fetchLogs(newDate);
    }

    private void onError(APIError apiError) {
        Toast.makeText(this.getActivity(), apiError.getUiMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INCLUDE_GROUPS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("INCLUDE GROUPS RESULT", "Success");
        }
    }

    private void attachListFragment() {
        Bundle data = new Bundle();
        data.putParcelableArrayList(LogConstants.LOGS, (ArrayList<LogDTO>) loggerViewModel.getLogs().getValue());
        logsListFragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.logsListFrame, logsListFragment);
        fragmentTransaction.commit();
    }

    private boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }

    private void updateTotalKcal(List<LogDTO> logs) {
        bindingFragment.totalCaloriesTextView.setText(
                logs.stream().mapToInt(LogDTO::getKcal).sum() + " Kcal"
        );
    }

    private void openDatePicker(View v) {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date")
                .build();

        datePicker.addOnPositiveButtonClickListener(picked -> {
            Long epoch = (Long) picked;
            Date pickedDate = new Date(epoch.longValue());
            LocalDate ld = pickedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            loggerViewModel.getCurrentDateInViewLive().setValue(ld);
        });
        datePicker.showNow(getChildFragmentManager(), "date_picker");
    }

    private void onChanged(List<LogDTO> logDTOS) {
        updateTotalKcal(logDTOS);
        logsListFragment.updateList(logDTOS);
    }
}