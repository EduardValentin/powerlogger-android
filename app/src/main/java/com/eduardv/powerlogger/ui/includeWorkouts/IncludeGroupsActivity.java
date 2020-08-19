package com.eduardv.powerlogger.ui.includeWorkouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.ui.logger.listeners.LogListListener;
import com.eduardv.powerlogger.ui.logger.listeners.OnEditLogListener;
import com.eduardv.powerlogger.utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class IncludeGroupsActivity extends AppCompatActivity implements OnEditLogListener, LogListListener {

    private IncludeGroupsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_groups_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.includeGroupsActivityContainer, IncludeGroupsFragment.newInstance())
                    .commitNow();
        }

        Intent intent = getIntent();
        viewModel = ViewModelProviders.of(this).get(IncludeGroupsViewModel.class);
        ArrayList<GroupDTO> groups = new ArrayList<>(intent.getParcelableArrayListExtra(Constants.GROUPS));
        ArrayList<ExerciseDTO> exercises = new ArrayList<>(intent.getParcelableArrayListExtra(Constants.EXERCISES));

        viewModel.setDate((LocalDate) intent.getExtras().getSerializable(Constants.DATE));

        viewModel.setGroups(groups);
        viewModel.setExercises(exercises);

    }

    @Override
    public void onEditLog(LogDTO log, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        viewModel.updateLogCalories(log, computedLog -> {
            log.setKcal(computedLog.getKcal());
            viewModel.replaceUnsavedLog(log);
            onSuccess.accept(log);
        });
    }

    @Override
    public void removeLog(LogDTO logDTO) {
        Map<String, LogDTO> logsMap = viewModel.getIncludedUnsavedLogsLiveData().getValue();
        Objects.requireNonNull(logsMap).remove(logDTO.getId());
        viewModel.getIncludedUnsavedLogsLiveData().setValue(logsMap);
    }

    @Override
    public void removeLogFromCache(int position) {
        viewModel.removeUnsavedLog(position);
    }

    @Override
    public void addLogToCache(LogDTO log) {
        viewModel.addUnsavedLog(log);
    }
}
