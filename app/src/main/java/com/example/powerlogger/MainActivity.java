package com.example.powerlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.repositories.LogRepository;
import com.example.powerlogger.ui.activity.ActivityFragment;
import com.example.powerlogger.ui.groups.GroupsFragment;
import com.example.powerlogger.ui.logger.EditLogFragment;
import com.example.powerlogger.ui.logger.LogConstants;
import com.example.powerlogger.ui.logger.LoggerFragment;
import com.example.powerlogger.ui.logger.OnEditExerciseFragmentSubmitListener;
import com.example.powerlogger.ui.logger.listeners.LogListListener;
import com.example.powerlogger.ui.logger.listeners.OnEditLogListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements OnEditExerciseFragmentSubmitListener, OnEditLogListener, LogListListener {
    public static final int TO_WORKOUTS_RC = 10;

    private MainActivityViewModel mainActivityViewModel;
    private ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();
    private LogRepository logRepository = LogRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onEditExercise(MinifiedExerciseDTO exercise, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.updateExercise(exercise.getId(), exercise, onSuccess, onError);
    }

    @Override
    public void onEditLog(LogDTO log, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        logRepository.updateLog(log, onSuccess, onError);
    }

    @Override
    public void removeLog(LogDTO logDTO) {
        logRepository.removeLog(logDTO.getId(), Optional.empty(), t -> {
            Toast.makeText(this, "Log could not be deleted", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoggerFragment.INCLUDE_GROUPS_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<LogDTO> parceableLogs = data.getParcelableArrayListExtra(LogConstants.LOGS);
            mainActivityViewModel.sendBatchLogs(parceableLogs);
        }

        if (requestCode == LoggerFragment.INCLUDE_GROUPS_REQUEST_CODE && resultCode == TO_WORKOUTS_RC) {
            Navigation.findNavController(findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.navigation_activity);
        }
    }
}
