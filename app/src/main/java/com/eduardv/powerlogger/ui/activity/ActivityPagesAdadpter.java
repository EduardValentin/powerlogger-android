package com.eduardv.powerlogger.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eduardv.powerlogger.ui.exercises.ExercisesFragment;
import com.eduardv.powerlogger.ui.groups.GroupsFragment;

public class ActivityPagesAdadpter extends FragmentStateAdapter {
    public ActivityPagesAdadpter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            Fragment fragment = new GroupsFragment();
            return fragment;
        }

        Fragment activityFragment = new ExercisesFragment();
        return activityFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
