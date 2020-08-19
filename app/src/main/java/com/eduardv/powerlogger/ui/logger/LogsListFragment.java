package com.eduardv.powerlogger.ui.logger;

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

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentLogsListBindingImpl;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.lib.lists.RecyclerItemTouchHelperListener;
import com.eduardv.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;
import com.eduardv.powerlogger.repositories.ExerciseCategoryRepository;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.ui.logger.listeners.LogListListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.Snackbar.Callback;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_TIMEOUT;


public class LogsListFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private List<LogDTO> logs;
    private FragmentLogsListBindingImpl bindingFragment;
    private LogListListener logListListener;
    private LogListAdapter logListAdapter;
    private Context context;
    private Snackbar snackbar;

    public LogsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            logs = getArguments().getParcelableArrayList(LogConstants.LOGS);
        } catch (NullPointerException e) {
            Log.e("PARCELABLE ERROR LOGS", "Can't parse logs list.");
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
        } else {
            bindingFragment.noLogsTextView.setVisibility(View.INVISIBLE);
        }

        this.logs = new ArrayList<>();
        this.logs.addAll(newLogs);
        this.logs.add(0, new LogDTO());

        this.logListAdapter = new LogListAdapter(this.logs, this::onLogClick);
        this.logListAdapter.setCategories(ExerciseCategoryRepository.getInstance().getCategoriesCache().getValue());

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context);

        bindingFragment.logsListView.setAdapter(logListAdapter);
        bindingFragment.logsListView.setHasFixedSize(false);
        bindingFragment.logsListView.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(logListAdapter);
        helper.attachToRecyclerView(bindingFragment.logsListView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        logListListener = (LogListListener) context;
    }

    private ItemTouchHelper createItemTouchHelper(RecyclerView.Adapter adapter) {
        return new ItemTouchHelper(
                new SwipeCallbackWithDeleteIcon(0, LEFT, this));
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LogListItemViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();

            // get the removed item name to display it in snack bar
            String name = logs.get(deletedIndex).getExercise().getName();

            // backup of removed item for undo purpose
            final LogDTO deletedItem = logs.get(deletedIndex);

            logListListener.removeLogFromCache(deletedIndex - 1);

            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }

            // showing snack bar with Undo option
            snackbar = Snackbar
                    .make(bindingFragment.getRoot(), name + " removed from the list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {
                logListListener.addLogToCache(deletedItem);
            });

            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Callback.DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_MANUAL) {
                        logListListener.removeLog(deletedItem);
                    }
                }
            });

            snackbar.show();
        }
    }
}
