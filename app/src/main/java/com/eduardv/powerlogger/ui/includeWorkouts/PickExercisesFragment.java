package com.eduardv.powerlogger.ui.includeWorkouts;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.PickExercisesFragmentBindingImpl;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.ui.logger.LogConstants;
import com.eduardv.powerlogger.ui.logger.LogsListFragment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PickExercisesFragment extends Fragment {

    private IncludeGroupsViewModel activityViewModel;
    private LogsListFragment logsListFragment;
    private PickExercisesFragmentBindingImpl bindingFragment;

    public PickExercisesFragment() {
        logsListFragment = new LogsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.pick_exercises_fragment, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        activityViewModel = ViewModelProviders.of(getActivity()).get(IncludeGroupsViewModel.class);
        activityViewModel.setIncludedUnsavedLogs(mapLogsListToHash(getPlaceholderLogsFromIncludedGroups()));

        attachLogListFragment();

        activityViewModel.getIncludedUnsavedLogsLiveData().observe(this, (unsavedLogs) -> {
            this.logsListFragment.updateList(new ArrayList<>(unsavedLogs.values()));
        });

        return view;
    }

    public void onAddExercisesConfirm() {
        List<LogDTO> includedLogs = new ArrayList<>(activityViewModel.getIncludedUnsavedLogsLiveData().getValue().values());
        Intent data = new Intent();
        data.putExtra(LogConstants.LOGS, (ArrayList<LogDTO>) includedLogs);

        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    private void attachLogListFragment() {
        Bundle data = new Bundle();
        data.putParcelableArrayList(LogConstants.LOGS, new ArrayList<>(activityViewModel.getIncludedUnsavedLogsLiveData().getValue().values()));
        logsListFragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.pickExercisesFrameLayout, logsListFragment);
        fragmentTransaction.commit();
    }

    private ArrayList<LogDTO> getPlaceholderLogsFromIncludedGroups() {
        ArrayList<LogDTO> unsavedLogs = new ArrayList<>();

        List<ExerciseDTO> exercises = activityViewModel.getExercises();
        activityViewModel.getCheckedGroups().forEach((groupUUID, isChecked) -> {
            if (isChecked) {
                List<LogDTO> groupUnsavedLogs = exercises.stream()
                        .filter(exercise -> exerciseIsInGroupWithId(exercise, groupUUID))
                        .map(this::mapExerciseToUnsavedLog).collect(Collectors.toList());

                unsavedLogs.addAll(groupUnsavedLogs);
            }
        });

        return unsavedLogs;
    }

    private LogDTO mapExerciseToUnsavedLog(ExerciseDTO ex) {
        LogDTO unsavedLog = new LogDTO();

        unsavedLog.setId(UUID.randomUUID().toString());
        unsavedLog.setKcal(0);
        unsavedLog.setCreatedAt(activityViewModel.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        unsavedLog.setNotes("");
        unsavedLog.setExercise(ex);

        return unsavedLog;
    }

    private boolean exerciseIsInGroupWithId(ExerciseDTO exerciseDTO, String groupUUID) {
        return exerciseDTO.getGroups().stream().anyMatch(grp -> grp.getId().equals(groupUUID));
    }

    private Map<String, LogDTO> mapLogsListToHash(List<LogDTO> list) {
        Map<String, LogDTO> logsMap = new HashMap<>();
        list.forEach(logDTO -> {
            logsMap.put(logDTO.getId(), logDTO);
        });
        return logsMap;
    }
}
