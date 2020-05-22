package com.example.powerlogger.ui.includeWorkouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.ui.logger.EditLogFragment;
import com.example.powerlogger.ui.logger.LogConstants;
import com.example.powerlogger.ui.logger.listeners.LogListListener;
import com.example.powerlogger.ui.logger.listeners.OnEditLogListener;
import com.example.powerlogger.utils.Constants;

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
}
