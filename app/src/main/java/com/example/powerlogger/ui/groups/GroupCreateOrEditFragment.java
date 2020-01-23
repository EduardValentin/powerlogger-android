package com.example.powerlogger.ui.groups;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.repositories.GroupRepository;

public class GroupCreateOrEditFragment extends Fragment {
    private String editGroupId;
    private  EditText groupNameEditText;
    private GroupRepository groupRepository = GroupRepository.getInstance();

    public GroupCreateOrEditFragment() {
        // Required empty public constructor
        Bundle data = getArguments();
        try {
            editGroupId = data.getString("groupId");
        } catch (NullPointerException e) {
            editGroupId = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_create_or_edit, container, false);

        Button saveGroup = view.findViewById(R.id.saveGroup);
        Button backButton = view.findViewById(R.id.addGroupBackBtn);
        groupNameEditText = view.findViewById(R.id.groupName);

        backButton.setOnClickListener(v -> popFragmentFromStack());

        if(editGroupId != null) {
            saveGroup.setText("Edit");
            saveGroup.setOnClickListener(v -> editGroup(editGroupId));
        } else {
            saveGroup.setText("Create");
            saveGroup.setOnClickListener(v -> createGroup());
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void handleError(Throwable t) {
        Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
        Log.e("Error", t.getMessage());
    }

    private void handleSuccessRequest() {
        popFragmentFromStack();
    }

    private void popFragmentFromStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void createGroup() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(groupNameEditText.getText().toString());
        groupRepository.createGroup(groupDTO, o -> handleSuccessRequest(), throwable -> handleError(throwable));
    }

    private void editGroup(String editGroupId) {
        GroupDTO groupDTO = new GroupDTO(editGroupId, groupNameEditText.getText().toString());
        groupRepository.updateGroup(groupDTO.getId(), groupDTO, o -> handleSuccessRequest(), throwable -> handleError(throwable));
    }
}
