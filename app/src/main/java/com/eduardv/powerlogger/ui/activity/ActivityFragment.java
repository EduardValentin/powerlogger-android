package com.eduardv.powerlogger.ui.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentActivityBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    private FragmentActivityBinding bindingFragment;
    private ActivityPagesAdadpter activityPagesAdadpter;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingFragment = DataBindingUtil.inflate(inflater,
                R.layout.fragment_activity, container, false);

        View view = bindingFragment.getRoot();
        bindingFragment.setView(this);
        bindingFragment.setLifecycleOwner(this);

        activityPagesAdadpter = new ActivityPagesAdadpter(this);
        bindingFragment.pager.setAdapter(activityPagesAdadpter);

        new TabLayoutMediator(bindingFragment.tabLayout, bindingFragment.pager, this::setTabText).attach();

        return view;
    }

    private TabLayout.Tab setTabText(TabLayout.Tab tab, int position) {
        if (position == 0) {
            return tab.setText(R.string.title_exercises);
        }

        return tab.setText(R.string.title_groups);
    }
}
