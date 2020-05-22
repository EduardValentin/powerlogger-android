package com.example.powerlogger.ui.exercises.edit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.EditExerciseFragmentBinding;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.model.ExerciseCategory;
import com.example.powerlogger.ui.exercises.ExercisesConstants;
import com.example.powerlogger.ui.logger.OnEditExerciseFragmentSubmitListener;
import com.example.powerlogger.utils.ArrayUtills;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditExerciseFragment extends Fragment {

    private EditExerciseViewModel mViewModel;
    private EditExerciseFragmentBinding binding;
    private List<GroupDTO> groups;
    private OnEditExerciseFragmentSubmitListener listener;

    public static EditExerciseFragment newInstance() {
        return new EditExerciseFragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (OnEditExerciseFragmentSubmitListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mViewModel = new ViewModelProvider(this).get(EditExerciseViewModel.class);

        Bundle data = getArguments();
        try {
            mViewModel.setExerciseDTO(data.getParcelable(ExercisesConstants.EXERCISE));
            groups = data.getParcelableArrayList(ExercisesConstants.GROUPS);
        } catch (NullPointerException e) {
            Log.e("PARCEABLE ERROR EDIT Exercise", "Can't parse edit exercise.");
            getActivity().getSupportFragmentManager().popBackStack();
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_exercise_fragment, container, false);
        binding.setModel(mViewModel);
        binding.setView(this);

        List<ExerciseCategory> exerciseCategories =
                Arrays.stream(ExerciseCategory.values())
                        .collect(Collectors.toList());

        binding.exerciseType.setTitle(getStringRes(R.string.exercise_type_title));
        binding.exerciseType.setItems(exerciseCategories);
        binding.exerciseType.setOnConfirmConsumer(this::onSelectCategory);
        binding.exerciseType.setSelectedItem(exerciseCategories.indexOf(mViewModel.getExerciseDTO().getCategory()));
        binding.exerciseType.updateInputValue();

        binding.exerciseGroup.setTitle(getStringRes(R.string.add_to_group_title));
        binding.exerciseGroup.setItems(groups);
        binding.exerciseGroup.setOnConfirmConsumer(this::onSelectGroups);
        binding.exerciseGroup.setSelectedItemsData(computeSelectedItems());

        mViewModel.getExerciseDTO().getGroups().forEach(this::drawChip);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditExerciseViewModel.class);
        // TODO: Use the ViewModel
    }

    private String getStringRes(int res) {
        return getResources().getString(res);
    }

    public void onSelectGroups(Object groups) {
        List<GroupDTO> selectedGroups = (List<GroupDTO>) groups;
        mViewModel.getExerciseDTO().setGroupIds(
                selectedGroups.stream().map(GroupDTO::getId).collect(Collectors.toList()));

        selectedGroups.forEach(this::drawChip);
    }

    public void onSelectCategory(Object category) {
        try {
            ExerciseCategory cat = (ExerciseCategory) category;
            mViewModel.getExerciseDTO().setCategory(cat);
        } catch (Exception ex) {
            Log.e("CATEGORY SELECT ERR:", ex.getMessage());
        }
    }

    public void onSave() {
        listener.onEditExercise(mViewModel.getExerciseDTO(), this::handleSuccess, this::handleError);
    }

    private void handleError(Throwable t) {
        Log.i("EDIT EXERCISE", "Error while editing exercise");
        Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(Object o) {
        Log.i("EDIT EXERCISE", "Success");
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private List<Boolean> computeSelectedItems() {
        List<Boolean> selectedItems = new ArrayList<>();

        for (GroupDTO cacheGroup : groups) {
            int foundIndex = ArrayUtills.findIndexByPredicate(mViewModel.getExerciseDTO().groups,
                    exerciseGroup -> exerciseGroup.getId().equals(cacheGroup.getId()));

            if (foundIndex != -1) {
                selectedItems.add(true);
            } else {
                selectedItems.add(false);
            }
        }

        return selectedItems;
    }

    private void drawChip(GroupDTO checkedGroup) {
        Chip chip = new Chip(getContext());
        chip.setText(checkedGroup.getName());
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            int index = ArrayUtills.findIndexByPredicate(
                    this.groups,
                    checkedGroup::equalIds
            );

            binding.exerciseGroup.setSelectedAtIndex(index, false);

            mViewModel.getExerciseDTO()
                    .getGroupIds()
                    .removeIf(checkedGroup.getId()::equals);

            binding.chipGroup.removeView(v);
        });

        binding.chipGroup.addView(chip);
    }
}
