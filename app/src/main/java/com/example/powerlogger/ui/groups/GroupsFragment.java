package com.example.powerlogger.ui.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.Group;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment {
    private ArrayList<Group> groupsArrayList;
//    private ListView groupsListView;
    private GroupsViewModel groupsViewModel;
    private ImageButton addGroupBtn;
    private ExpandingList expandingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
        addGroupBtn = view.findViewById(R.id.addGroupButton);
//        groupsListView = view.findViewById(R.id.groupsListView);
        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);

//        groupsListView.setOnItemClickListener((parent, view1, position, id) -> {
//            GroupDTO groupDTO = groupsViewModel.getGroupsLiveData().getValue().get(position);
//            onEditGroup(groupDTO);
//        });

        addGroupBtn.setOnClickListener(v -> onAddNewGroup());

//        updateListView(groupsViewModel.getGroupsLiveData().getValue());
//
//        groupsViewModel.getGroupsLiveData().observe(this, groupDTOS -> {
//            updateListView(groupDTOS);
//        });

        expandingList = view.findViewById(R.id.expanding_list_main);
        renderGroups(groupsViewModel.getGroupsLiveData().getValue());
        groupsViewModel.getGroupsLiveData().observe(this, groups -> renderGroups(groups));
        return view;
    }

    public void onAddNewGroup() {
        Fragment createGroupFragment = new GroupCreateOrEditFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, createGroupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void renderGroups(List<GroupDTO> groups) {
        expandingList.removeAllViews();

        groups.forEach(groupDTO -> {
            ExpandingItem item = expandingList.createNewItem(R.layout.expandable_layout);

            ((TextView) item.findViewById(R.id.title))
                    .setText(groupDTO.getName());

            List<LogDTO> logs = groupDTO.getLogsList();

            item.createSubItems(logs.size());

            Button groupEditBtn = item.findViewById(R.id.groupEditButton);
            groupEditBtn.setOnClickListener(v -> onEditGroup(groupDTO));

            for (int i = 0; i < logs.size(); i++) {
                View subItemView = item.getSubItemView(i);

                TextView title = subItemView.findViewById(R.id.sub_title);
                title.setText(logs.get(i).getName());


            }
        });
    }

    public void onEditGroup(GroupDTO groupDTO) {
        Fragment editGroupFragment = new GroupCreateOrEditFragment();
        Bundle data = new Bundle();
        data.putString("groupId", groupDTO.getId());

        editGroupFragment.setArguments(data);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, editGroupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
