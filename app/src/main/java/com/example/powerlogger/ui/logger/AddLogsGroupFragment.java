package com.example.powerlogger.ui.logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.IncludeGroupsDTO;
import com.example.powerlogger.ui.groups.GroupsViewModel;
import com.example.powerlogger.ui.login.LoginViewModel;
import com.example.powerlogger.ui.login.LoginViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class AddLogsGroupFragment extends Fragment {

    private GroupsViewModel groupsViewModel;
    private ExpandingList expandingList;
    List<String> checkedGroupsIds = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_group, container, false);

        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);

        expandingList = view.findViewById(R.id.expanding_list_select);

        renderGroups(groupsViewModel.getGroupsLiveData().getValue());
        groupsViewModel.getGroupsLiveData().observe(this, groups -> renderGroups(groups));

        Button addBtn = view.findViewById(R.id.addLogsGroupButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IncludeGroupsDTO includeGroupsDTO = new IncludeGroupsDTO();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                includeGroupsDTO.setDate(date);

                includeGroupsDTO.setCheckedGroupsIds(checkedGroupsIds);

//                //testare
//                System.out.println("TESTARE---------------------");
//                for(String elem : includeGroupsDTO.getCheckedGroupsIds()) {
//                    System.out.println(elem);
//                }
            }
        });

        return view;

    }

    private void renderGroups(List<GroupDTO> groups) {

        groups.forEach(groupDTO -> {

            ExpandingItem item = expandingList.createNewItem(R.layout.include_groups_expandable_layout);
            CheckBox cb = (CheckBox) item.findViewById(R.id.checkbox_group);
            cb.setText(groupDTO.getName());
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedGroupsIds.add(groupDTO.getId());
                    }
                    else{
                        if(checkedGroupsIds.contains(groupDTO.getId())){
                            checkedGroupsIds.remove(groupDTO.getId());
                        }

                    }
                }
            });

            //((TextView) item.findViewById(R.id.title))
              //      .setText(groupDTO.getName());

            // Handle log subitems

            item.createSubItems(2);

            View subItemZero = item.getSubItemView(0);
            ((TextView) subItemZero.findViewById(R.id.g_sub_title)).setText("Cool");

            View subItemOne = item.getSubItemView(1);
            ((TextView) subItemOne.findViewById(R.id.g_sub_title)).setText("Awesome");
        });
    }
}
