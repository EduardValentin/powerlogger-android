package com.eduardv.powerlogger.ui.exercises.create;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.CreateExerciseFragmentBinding;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.SelectableItem;
import com.eduardv.powerlogger.model.ExerciseCategory;
import com.eduardv.powerlogger.utils.ArrayUtills;
import com.eduardv.powerlogger.utils.StringUtils;
import com.google.android.material.chip.Chip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static android.widget.Toast.LENGTH_SHORT;

public class CreateExerciseFragment extends Fragment {

    private CreateExerciseViewModel createExerciseViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private CreateExerciseFragmentBinding binding;

    public static CreateExerciseFragment newInstance() {
        return new CreateExerciseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        createExerciseViewModel = new ViewModelProvider(this)
                .get(CreateExerciseViewModel.class);

        mainActivityViewModel = new ViewModelProvider(getActivity())
                .get(MainActivityViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.create_exercise_fragment, container, false);
        binding.setViewmodel(createExerciseViewModel);
        binding.setView(this);

        mainActivityViewModel.getGroupsLiveData().observe(getViewLifecycleOwner(), binding.exerciseGroup::setItems);
        binding.exerciseGroup.setOnConfirmConsumer(this::onSelectGroups);
        binding.exerciseGroup.setTitle(getStringRes(R.string.add_to_group_title));
        binding.exerciseType.setOnConfirmConsumer(this::onSelectCategory);

        mainActivityViewModel.getExerciseCategoriesLive().observe(getViewLifecycleOwner(), binding.exerciseType::setItems);
        binding.exerciseType.setTitle(getStringRes(R.string.exercise_type_title));


        return binding.getRoot();
    }

    public void onCreateExerciseClick(View v) {
        boolean hasErrors = this.hasErrors();

        if (hasErrors) {
            return;
        }

        createExerciseViewModel.sendCreateExercise(this::onSuccess, this::onSendError);
    }

    private boolean hasErrors() {
        CreateExerciseRequestDTO creatingExercise = createExerciseViewModel.getExercise();
        boolean hasErrors = false;

        if (creatingExercise.getCategory() == null) {
            binding.exerciseTypeLayout.setError("Exercise type is required");
            hasErrors = true;
        } else {
            binding.exerciseTypeLayout.setError(null);
        }

        if (StringUtils.isEmpty(creatingExercise.getName())) {
            binding.exerciseNameLayout.setError("Exercise name is required");
            hasErrors = true;
        } else {
            binding.exerciseNameLayout.setError(null);
        }

        return hasErrors;
    }

    private void onSendError(Throwable t) {
        Toast.makeText(getContext(), R.string.something_wrong_happened, LENGTH_SHORT).show();
    }

    private void onSuccess(Object res) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void onSelectGroups(List<? extends SelectableItem> selectableItems) {
        List<GroupDTO> selectedGroups = (List<GroupDTO>) selectableItems;

        createExerciseViewModel.getExercise().setGroupIds(
                selectedGroups.stream().map(GroupDTO::getId).collect(Collectors.toList()));

        binding.chipGroup.removeAllViews();

        selectedGroups.forEach(checkedGroup -> {

            Chip chip = new Chip(getContext());
            chip.setText(checkedGroup.getName());
            chip.setCloseIconVisible(true);

            chip.setOnCloseIconClickListener(v -> {
                int index = ArrayUtills.findIndexByPredicate(
                        mainActivityViewModel.getGroupsLiveData().getValue(),
                        checkedGroup::equalIds
                );

                binding.exerciseGroup.setSelectedAtIndex(index, false);

                createExerciseViewModel.getExercise()
                        .getGroupIds()
                        .removeIf(checkedGroup.getId()::equals);

                binding.chipGroup.removeView(v);
            });

            binding.chipGroup.addView(chip);
        });
    }

    public void onSelectCategory(Object category) {
        try {
            ExerciseCategoryDTO cat = (ExerciseCategoryDTO) category;
            createExerciseViewModel.getExercise().setCategory(cat);
        } catch (Exception ex) {
            Log.e("CATEGORY SELECT ERR:", ex.getMessage());
        }
    }

    private String getStringRes(int res) {
        return getResources().getString(res);
    }
}
