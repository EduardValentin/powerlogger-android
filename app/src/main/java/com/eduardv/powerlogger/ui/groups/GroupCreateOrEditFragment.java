package com.eduardv.powerlogger.ui.groups;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentGroupCreateOrEditBinding;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.SelectableItem;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.repositories.GroupRepository;
import com.eduardv.powerlogger.utils.ArrayUtills;
import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupCreateOrEditFragment extends Fragment {
    private GroupDTO groupDTO;
    private FragmentGroupCreateOrEditBinding binding;
    private GroupCreateOrEditViewModel viewModel;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle data = getArguments();

        if (data != null) {
            groupDTO = data.getParcelable("group");
        }

        viewModel = new ViewModelProvider(this).get(GroupCreateOrEditViewModel.class);
        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_create_or_edit, container, false);
        binding.setView(this);
        binding.setModel(viewModel);

        binding.groupExercises.setItems(getExercisesFromModelLiveData());
        binding.groupExercises.setOnConfirmConsumer(this::onSelectExercises);

        if (groupDTO != null) {
            handleUIForEdit();
        } else {
            handleUIForCreate();
        }

        return binding.getRoot();
    }

    private void handleError(Throwable t) {
        Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
        Log.e("Error", t.getMessage());
    }

    private void handleSuccessRequest(GroupDTO groupDTO) {
        popFragmentFromStack();
    }

    private void popFragmentFromStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleUIForEdit() {
        viewModel.setName(groupDTO.getName());

        List<ExerciseDTO> groupExercises = getGroupExercises();
        Map<String, Integer> indexMap = ExerciseRepository.getMappingFromExerciseIdToIndex(getExercisesFromModelLiveData());

        groupExercises.forEach(exercise -> {
            addExerciseToChip(exercise);
            Integer index = indexMap.get(exercise.getId());
            binding.groupExercises.setSelectedAtIndex(index, true);
        });

        viewModel.setAddedExercises(groupExercises);
        binding.saveGroup.setOnClickListener(v -> editGroup(groupDTO.getId()));
    }

    private void handleUIForCreate() {
        binding.saveGroup.setOnClickListener(v -> createGroup());
    }

    private void createGroup() {
        String groupName = viewModel.getName();

        if (groupName == null || groupName.length() == 0) {
            binding.groupNameLayout.setError("Workout name cannot be empty");
            return;
        }

        viewModel.addGroup(this::handleSuccessRequest, this::handleError);
    }

    private void editGroup(String id) {
        String groupName = viewModel.getName();

        if (groupName == null || groupName.length() == 0) {
            binding.groupNameLayout.setError("Workout name cannot be empty");
            return;
        }

        viewModel.editGroup(id, this::handleSuccessRequest, this::handleError);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onSelectExercises(List<? extends SelectableItem> selectableItems) {
        binding.chipGroup.removeAllViews();
        List<ExerciseDTO> exercises = (List<ExerciseDTO>) selectableItems;
        exercises.forEach(this::addExerciseToChip);

        viewModel.setAddedExercises(exercises);
    }

    private List<ExerciseDTO> getGroupExercises() {
        return getExercisesFromModelLiveData()
                .stream()
                .filter(this::exerciseHasThisGroup)
                .collect(Collectors.toList());
    }

    private List<ExerciseDTO> getExercisesFromModelLiveData() {
        return mainActivityViewModel.getExerciseLiveData().getValue();
    }

    private boolean exerciseHasThisGroup(ExerciseDTO exercise) {
        if (exercise.getGroups() == null) {
            return false;
        }

        return exercise.getGroups().stream().anyMatch(groupDTO::equalIds);
    }

    private void addExerciseToChip(ExerciseDTO exercise) {
        Chip chip = new Chip(getContext());
        chip.setText(exercise.getName());
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            int index = ArrayUtills.findIndexByPredicate(
                    getExercisesFromModelLiveData(),
                    exercise::equalIds
            );

            binding.groupExercises.setSelectedAtIndex(index, false);

            viewModel.getAddedExercises()
                    .removeIf(exercise::equalIds);

            binding.chipGroup.removeView(v);
        });

        binding.chipGroup.addView(chip);
    }

    // To avoid O(N^2) searching for selected items
    private Map<String, Integer> getMappingFromExerciseIdToIndex(List<ExerciseDTO> exercises) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < exercises.size(); i++) {
            indexMap.put(exercises.get(i).getId(), i);
        }
        return indexMap;
    }
}
