package com.eduardv.powerlogger.ui.exercises.edit;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.EditExerciseFragmentBinding;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.model.ExerciseCategory;
import com.eduardv.powerlogger.ui.exercises.ExercisesConstants;
import com.eduardv.powerlogger.ui.logger.OnEditExerciseFragmentSubmitListener;
import com.eduardv.powerlogger.utils.ArrayUtills;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditExerciseFragment extends Fragment {

    private EditExerciseViewModel mViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private EditExerciseFragmentBinding binding;
    private List<GroupDTO> groups;
    private OnEditExerciseFragmentSubmitListener listener;


    private TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable groupName) {
            String groupNameStr = groupName.toString();

            if (groupNameStr.isEmpty()) {
                binding.exerciseNameLayout.setError("Exercise name cannot be empty");
                binding.button.setEnabled(false);
            } else {
                binding.exerciseNameLayout.setError(null);
                binding.button.setEnabled(true);
            }
        }
    };

    public static EditExerciseFragment newInstance() {
        return new EditExerciseFragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (OnEditExerciseFragmentSubmitListener) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mViewModel = new ViewModelProvider(this).get(EditExerciseViewModel.class);
        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        Bundle data = getArguments();
        try {
            mViewModel.setExerciseDTO(data.getParcelable(ExercisesConstants.EXERCISE));
            groups = data.getParcelableArrayList(ExercisesConstants.GROUPS);
        } catch (NullPointerException e) {
            Log.e("ERROR EDIT EXERCISE", "Can't parse edit exercise.");
            getActivity().getSupportFragmentManager().popBackStack();
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_exercise_fragment, container, false);
        binding.setModel(mViewModel);
        binding.setView(this);


        binding.exerciseType.setTitle(getStringRes(R.string.exercise_type_title));
        List<ExerciseCategoryDTO> categories = mainActivityViewModel.getExerciseCategoriesLive().getValue();

        int catIndex = ArrayUtills.findIndexByPredicate(categories, cat -> cat.getIdentifierName()
                .equals(mViewModel.getExerciseDTO().getCategory().getIdentifierName()));

        binding.exerciseType.setItems(categories);
        mainActivityViewModel.getExerciseCategoriesLive().observe(getViewLifecycleOwner(), binding.exerciseType::setItems);

        binding.exerciseType.setOnConfirmConsumer(this::onSelectCategory);

        if (catIndex != -1) {
            binding.exerciseType.setSelectedItem(catIndex);
        }

        int selectedCategoryIndex = ArrayUtills.findIndexByPredicate(mainActivityViewModel.getExerciseCategoriesLive().getValue(),
                exerciseCategoryDTO -> exerciseCategoryDTO.getIdentifierName().equals(mViewModel.getExerciseDTO().getCategory()));

        if (selectedCategoryIndex != -1) {
            binding.exerciseType.setSelectedItem(selectedCategoryIndex);
        }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSelectGroups(Object groups) {
        List<GroupDTO> selectedGroups = (List<GroupDTO>) groups;
        mViewModel.getExerciseDTO().setGroupIds(
                selectedGroups.stream().map(GroupDTO::getId).collect(Collectors.toList()));

        binding.chipGroup.removeAllViews();
        selectedGroups.forEach(this::drawChip);
    }

    public void onSelectCategory(Object category) {
        try {
            ExerciseCategoryDTO cat = (ExerciseCategoryDTO) category;
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

    public TextWatcher getAfterTextChangedListener() {
        return afterTextChangedListener;
    }

}
