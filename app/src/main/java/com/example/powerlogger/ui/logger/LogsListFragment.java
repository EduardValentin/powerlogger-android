package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentLogsListBindingImpl;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;
import com.example.powerlogger.ui.logger.listeners.LogListListener;

import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.*;


public class LogsListFragment extends Fragment {
    private List<LogDTO> logs;
    private FragmentLogsListBindingImpl bindingFragment;
    private LogListListener logListListener;

    public LogsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            logs = getArguments().getParcelableArrayList(LogConstants.LOGS);
        } catch (NullPointerException e) {
            Log.e("PARCEABLE ERROR LIST LOGS", "Can't parse logs list.");
            getParentFragment().getParentFragmentManager().popBackStack();
        }

        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_logs_list, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        updateList(logs);

        return view;
    }

    public void updateList(List<LogDTO> newLogs) {
        if (bindingFragment == null) {
            return;
        }

        if (newLogs.size() == 0) {
            bindingFragment.noLogsTextView.setVisibility(View.VISIBLE);
            bindingFragment.logsListView.setVisibility(View.INVISIBLE);
        } else {
            bindingFragment.noLogsTextView.setVisibility(View.INVISIBLE);
            bindingFragment.logsListView.setVisibility(View.VISIBLE);
        }

        this.logs = newLogs;

        LogListAdapter logListAdapter = new LogListAdapter(newLogs, this::onLogClick);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        bindingFragment.logsListView.setAdapter(logListAdapter);
        bindingFragment.logsListView.setHasFixedSize(false);
        bindingFragment.logsListView.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(logListAdapter);
        helper.attachToRecyclerView(bindingFragment.logsListView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        logListListener = (LogListListener) context;
    }

    public void removeLogAtPosition(int pos) {
        logListListener.removeLog(logs.get(pos));
    }

    private ItemTouchHelper createItemTouchHelper(RecyclerView.Adapter adapter) {
        return new ItemTouchHelper(
                new SwipeCallbackWithDeleteIcon(0, LEFT, adapter, this::removeLogAtPosition, getContext()));
    }

    private void onLogClick(View v) {
        Integer position = (Integer) v.getTag();

        Fragment editLogFragment = new EditLogFragment();

        Bundle data = new Bundle();
        data.putParcelable(LogConstants.LOG_BUNDLE_KEY, logs.get(position));
        editLogFragment.setArguments(data);

        Fragment parentFragment = getParentFragment();
        FragmentTransaction fragmentTransaction;

        if (parentFragment != null) {
            fragmentTransaction = parentFragment.getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, editLogFragment);
        } else {
            fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.includeGroupsActivityContainer, editLogFragment);
        }

        fragmentTransaction.addToBackStack(null).commit();

    }
}
