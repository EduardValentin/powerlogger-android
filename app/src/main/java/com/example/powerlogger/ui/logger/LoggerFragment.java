package com.example.powerlogger.ui.logger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.R;

public class LoggerFragment extends Fragment {

    private LoggerViewModel loggerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loggerViewModel =
                ViewModelProviders.of(this).get(LoggerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logger, container, false);
//        final TextView textView = root.findViewById(R.id.text_logger);
//        loggerViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        AppCompatImageButton addNewExerciceBtn = root.findViewById(R.id.addLogButton);

        addNewExerciceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment AddLogFragment = new AddLogFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, AddLogFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;
    }
}