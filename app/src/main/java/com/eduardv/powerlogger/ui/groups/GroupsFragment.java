package com.eduardv.powerlogger.ui.groups;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentGroupsBindingImpl;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.lists.RecyclerItemTouchHelperListener;
import com.eduardv.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;


public class GroupsFragment extends Fragment implements RecyclerItemTouchHelperListener {
    private GroupsViewModel groupsViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private FragmentGroupsBindingImpl bindingFragment;
    private List<GroupDTO> groups;
    private GroupsAdapter groupsAdapter;
    private Snackbar snackbar;

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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof GroupsViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();

            // get the removed item name to display it in snack bar
            String name = groups.get(deletedIndex).getName();

            // backup of removed item for undo purpose
            final GroupDTO deletedItem = groups.get(deletedIndex);

            // remove the item from recycler view
//            groupsAdapter.removeItem(deletedIndex);
            groupsViewModel.removeGroupFromCache(deletedIndex - 1);

            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }

            // showing snack bar with Undo option
            snackbar = Snackbar
                    .make(bindingFragment.getRoot(), name + " removed from the list!", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> {
                // undo is selected, restore the deleted item
//                groupsAdapter.restoreItem(deletedItem, deletedIndex);
                groupsViewModel.addGroupToCache(deletedItem);
            });

            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_MANUAL) {
                        deleteGroup(deletedItem);
                    }
                }
            });
            snackbar.show();
        }
    }


    private void renderGroups(List<GroupDTO> groups) {
        if (groups.isEmpty()) {
            bindingFragment.noGroupsCreated.setVisibility(View.VISIBLE);
        }

        this.groups = new ArrayList<>();
        this.groups.add(new GroupDTO());
        this.groups.addAll(groups);


        groupsAdapter = new GroupsAdapter(this.groups, this::onGroupClick);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        bindingFragment.groupsList.setAdapter(groupsAdapter);
        bindingFragment.groupsList.setHasFixedSize(false);
        bindingFragment.groupsList.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(groupsAdapter);
        helper.attachToRecyclerView(bindingFragment.groupsList);
    }

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
                new SwipeCallbackWithDeleteIcon(0, LEFT, this));
    }

    private void deleteGroup(GroupDTO groupDTO) {

        mainActivityViewModel.removeGroup(groupDTO.getId(), null,
                t -> Log.e("REMOVE_GROUP", "Remove group failed", t));
    }

    private void onGroupClick(View v) {
        int position = (Integer) v.getTag();
        this.onEditGroup(this.groups.get(position));
    }

}
