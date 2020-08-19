package com.eduardv.powerlogger.ui.includeWorkouts;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.eduardv.powerlogger.MainActivity;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.IncludeGroupsFragmentBindingImpl;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncludeGroupsFragment extends Fragment {


    private IncludeGroupsViewModel activityViewModel;
    private IncludeGroupsFragmentBindingImpl bindingFragment;

    public static IncludeGroupsFragment newInstance() {
        return new IncludeGroupsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activityViewModel = ViewModelProviders.of(getActivity()).get(IncludeGroupsViewModel.class);

        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.include_groups_fragment, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        renderGroups(activityViewModel.getGroups());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        renderGroups(activityViewModel.getGroups());
    }

    private void renderGroups(List<GroupDTO> groups) {
        bindingFragment.includeGroupsList.removeAllViews();
        boolean atLeastOneGroup = false;

        for (GroupDTO groupDTO : groups) {

            // Find all exercises for this group
            List<ExerciseDTO> exercises = new ArrayList<>();
            activityViewModel.getExercises().stream().forEach(exerciseDTO -> {
                if (exerciseDTO == null) return;

                List<GroupDTO> filtered = exerciseDTO.getGroups().stream()
                        .filter(group -> group.getId().equals(groupDTO.getId()))
                        .collect(Collectors.toList());

                if (filtered.size() > 0) {
                    exercises.add(exerciseDTO);
                }
            });

            if (exercises.size() == 0) continue;

            atLeastOneGroup = true;

            ExpandingItem item = bindingFragment.includeGroupsList.createNewItem(R.layout.include_groups_expandable_layout);
            item.removeAllSubItems();

            ((TextView) item.findViewById(R.id.igItemTitle))
                    .setText(groupDTO.getName());

            Map<String, Boolean> checkedGroups = activityViewModel.getCheckedGroups();
            CheckBox checkBox = item.findViewById(R.id.igItemCheckbox);

            checkBox.setOnClickListener((view) -> {
                CheckBox c = (CheckBox) view;
                checkedGroups.put(groupDTO.getId(), c.isChecked());
            });

            if (checkedGroups.containsKey(groupDTO.getId())) {
                checkBox.setChecked(checkedGroups.get(groupDTO.getId()));
            }

            item.createSubItems(exercises.size());

            for (int i = 0; i < exercises.size(); i++) {
                View subItemView = item.getSubItemView(i);

                TextView title = subItemView.findViewById(R.id.igSubitemTitle);
                title.setText(exercises.get(i).getName());
            }
        }

        if (!atLeastOneGroup) {
            bindingFragment.noWorkoutsLayout.setVisibility(View.VISIBLE);
            bindingFragment.includeGroupsList.setVisibility(View.INVISIBLE);
            bindingFragment.pickExercisesButton.setEnabled(false);
        } else {
            bindingFragment.noWorkoutsLayout.setVisibility(View.INVISIBLE);
            bindingFragment.includeGroupsList.setVisibility(View.VISIBLE);
            bindingFragment.pickExercisesButton.setEnabled(true);
        }
    }

    public void onPickExercisesClick() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.includeGroupsActivityContainer, new PickExercisesFragment());
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void goBack() {
        getActivity().setResult(MainActivity.TO_WORKOUTS_RC);
        getActivity().finish();
    }
}
