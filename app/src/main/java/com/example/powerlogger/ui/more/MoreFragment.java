package com.example.powerlogger.ui.more;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.MoreFragmentBinding;
import com.example.powerlogger.repositories.UserRepository;
import com.example.powerlogger.ui.login.LoginActivity;
import com.example.powerlogger.ui.profile.ProfileFragment;
import com.example.powerlogger.ui.units.UnitsFragment;
import com.example.powerlogger.utils.Constants;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MoreFragment extends Fragment {

    private MoreFragmentBinding binding;
    private List<String> listItems = Arrays.asList(MoreItemType.PROFILE.getName(), MoreItemType.UNITS.getName(), MoreItemType.LOG_OUT.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.more_fragment, container, false);
        binding.setView(this);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listItems);
        binding.moreListView.setAdapter(adapter);
        binding.moreListView.setOnItemClickListener(this::onItemClick);
        return binding.getRoot();
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MoreItemType.fromString(listItems.get(position)).ifPresent(moreItemType -> {
            switch (moreItemType) {
                case UNITS:
                    goToItemTab(new UnitsFragment());
                    break;
                case LOG_OUT:
                    UserRepository.getInstance().logout();
                    getActivity()
                            .getSharedPreferences(Constants.USER_INFO, MODE_PRIVATE)
                            .edit().clear().apply();

                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    startActivity(loginIntent);
                    getActivity().finish();
                    break;
                case PROFILE:
                    goToItemTab(new ProfileFragment());
                    break;
            }
        });
    }

    private void goToItemTab(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null).commit();
    }
}
