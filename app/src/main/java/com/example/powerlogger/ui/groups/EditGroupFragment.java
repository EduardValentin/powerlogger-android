package com.example.powerlogger.ui.groups;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.EditGroupFragmentBinding;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.lib.SelectableItem;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.utils.ArrayUtills;
import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditGroupFragment extends Fragment {

    private EditGroupViewModel mViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private EditGroupFragmentBinding binding;
    private GroupDTO groupDTO;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        groupDTO = (GroupDTO) getArguments().get("group");

        mViewModel = new ViewModelProvider(this).get(EditGroupViewModel.class);
        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_group_fragment, container, false);
        binding.setView(this);
        binding.setModel(mViewModel);


        mViewModel.setName(groupDTO.getName());

        binding.groupExercises.setItems(getExercisesFromModelLiveData());
        binding.groupExercises.setOnConfirmConsumer(this::onSelectExercises);

        List<ExerciseDTO> groupExercises = getGroupExercises();
        Map<String, Integer> indexMap = ExerciseRepository.getMappingFromExerciseIdToIndex(getExercisesFromModelLiveData());

        groupExercises.forEach(exercise -> {
            addExerciseToChip(exercise);
            Integer index = indexMap.get(exercise.getId());
            binding.groupExercises.setSelectedAtIndex(index, true);
        });

        return binding.getRoot();
    }

    private void onSelectExercises(List<? extends SelectableItem> selectableItems) {
        binding.chipGroup.removeAllViews();

        List<ExerciseDTO> exercises = (List<ExerciseDTO>) selectableItems;

        exercises.forEach(this::addExerciseToChip);
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

            mViewModel.getAddedExercises()
                    .removeIf(exercise::equalIds);

            binding.chipGroup.removeView(v);
        });

        binding.chipGroup.addView(chip);
    }
}
