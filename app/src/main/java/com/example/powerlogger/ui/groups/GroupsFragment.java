package com.example.powerlogger.ui.groups;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentGroupsBindingImpl;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;

import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;


public class GroupsFragment extends Fragment {
    private GroupsViewModel groupsViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private FragmentGroupsBindingImpl bindingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_groups, container, false);

        View view = bindingFragment.getRoot();

        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this.getActivity()).get(MainActivityViewModel.class);

        mainActivityViewModel.getGroupsLiveData().observe(getViewLifecycleOwner(), this::renderGroups);

        return view;
    }

    public void onAddNewGroup(View v) {
        Fragment createGroupFragment = new GroupCreateOrEditFragment();
        FragmentTransaction fragmentTransaction = getParentFragment().getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, createGroupFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void renderGroups(List<GroupDTO> groups) {

        GroupsAdapter adapter = new GroupsAdapter(groups, this::onGroupClick);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        bindingFragment.groupsList.setAdapter(adapter);
        bindingFragment.groupsList.setHasFixedSize(false);
        bindingFragment.groupsList.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(adapter);
        helper.attachToRecyclerView(bindingFragment.groupsList);
    }

//    private void renderGroups(List<GroupDTO> groups) {
//        bindingFragment.expandingListMain.removeAllViews();
//
//        groups.forEach(groupDTO -> {
//            ExpandingItem item = bindingFragment.expandingListMain.createNewItem(R.layout.expandable_layout);
//
//            ((TextView) item.findViewById(R.id.exerciseTitle))
//                    .setText(groupDTO.getName());
//
//            List<ExerciseDTO> exercises = new ArrayList<>();
//            mainActivityViewModel.getExerciseLiveData().getValue().forEach(exerciseDTO -> {
//                if (exerciseDTO.getGroups() == null) {
//                    return;
//                }
//
//                List<GroupDTO> filtered = exerciseDTO.getGroups().stream()
//                        .filter(group -> group.getId().equals(groupDTO.getId()))
//                        .collect(Collectors.toList());
//
//                if (filtered.size() > 0) {
//                    exercises.add(exerciseDTO);
//                }
//            });
//
//            Button groupEditBtn = item.findViewById(R.id.groupEditButton);
//            groupEditBtn.setOnClickListener(v -> onEditGroup(groupDTO));
//
//            if (exercises.size() == 0) return;
//            item.createSubItems(exercises.size());
//
//            for (int i = 0; i < exercises.size(); i++) {
//                View subItemView = item.getSubItemView(i);
//
//                final ExerciseDTO thisExercise = exercises.get(i);
//
//                TextView title = subItemView.findViewById(R.id.sub_title);
//                title.setText(exercises.get(i).getName());
//
//                ImageButton closeButton = subItemView.findViewById(R.id.deleteExerciseFromGroup);
//                closeButton.setOnClickListener(v ->
//                        groupsViewModel.removeExerciseFromGroup(thisExercise.getId(),
//                                groupDTO.getId(), o -> item.removeSubItem(subItemView), null));
//            }
//        });
//    }

    private void onEditGroup(GroupDTO groupDTO) {
        Fragment editGroupFragment = new GroupCreateOrEditFragment();
        Bundle data = new Bundle();
        data.putParcelable("group", groupDTO);

        editGroupFragment.setArguments(data);
        FragmentTransaction fragmentTransaction = getParentFragment().getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, editGroupFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private ItemTouchHelper createItemTouchHelper(RecyclerView.Adapter adapter) {
        return new ItemTouchHelper(
                new SwipeCallbackWithDeleteIcon(0, LEFT, adapter, this::deleteGroup, getContext()));
    }

    private void deleteGroup(Integer position) {

        mainActivityViewModel.removeGroup(getGroups().get(position).getId(), null,
                t -> Log.e("REMOVE_GROUP", "Remove group failed", t));
    }

    private List<GroupDTO> getGroups() {
        return mainActivityViewModel.getGroupsLiveData().getValue();
    }

    private void onGroupClick(View v) {
        Integer position = (Integer) v.getTag();
        this.onEditGroup(getGroups().get(position));
    }
}
