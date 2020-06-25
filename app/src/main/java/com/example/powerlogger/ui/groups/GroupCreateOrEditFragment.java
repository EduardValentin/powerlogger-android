package com.example.powerlogger.ui.groups;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.powerlogger.MainActivity;
import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentGroupCreateOrEditBinding;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.lib.SelectableItem;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.utils.ArrayUtills;
import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupCreateOrEditFragment extends Fragment {
    private GroupDTO groupDTO;
    private GroupRepository groupRepository = GroupRepository.getInstance();
    private FragmentGroupCreateOrEditBinding binding;
    private GroupCreateOrEditViewModel viewModel;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    private void handleUIForEdit() {
        binding.saveGroup.setText("Edit");

        viewModel.setName(groupDTO.getName());

        List<ExerciseDTO> groupExercises = getGroupExercises();
        Map<String, Integer> indexMap = ExerciseRepository.getMappingFromExerciseIdToIndex(getExercisesFromModelLiveData());

        groupExercises.forEach(exercise -> {
            addExerciseToChip(exercise);
            Integer index = indexMap.get(exercise.getId());
            binding.groupExercises.setSelectedAtIndex(index, true);
        });

        viewModel.setAddedExercises(groupExercises);
        binding.saveGroup.setOnClickListener(v -> editGroup(groupDTO));
    }

    private void handleUIForCreate() {
        binding.saveGroup.setText("Create");
        binding.saveGroup.setOnClickListener(v -> createGroup());
    }

    private void createGroup() {
        viewModel.addGroup(this::handleSuccessRequest, this::handleError);
    }

    private void editGroup(GroupDTO groupDTO) {
        viewModel.editGroup(groupDTO.getId(), this::handleSuccessRequest, this::handleError);
    }

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
