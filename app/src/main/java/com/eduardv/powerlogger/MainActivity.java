package com.eduardv.powerlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.repositories.LogRepository;
import com.eduardv.powerlogger.ui.logger.LogConstants;
import com.eduardv.powerlogger.ui.logger.LoggerFragment;
import com.eduardv.powerlogger.ui.logger.OnEditExerciseFragmentSubmitListener;
import com.eduardv.powerlogger.ui.logger.listeners.LogListListener;
import com.eduardv.powerlogger.ui.logger.listeners.OnEditLogListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements OnEditExerciseFragmentSubmitListener, OnEditLogListener, LogListListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final int TO_WORKOUTS_RC = 10;

    private MainActivityViewModel mainActivityViewModel;
    private ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();
    private LogRepository logRepository = LogRepository.getInstance();
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        MobileAds.initialize(this, initializationStatus -> {
        });

        this.rewardedAd = createAndLoadRewardedAds();
    }

    public RewardedAd createAndLoadRewardedAds() {
        RewardedAd rewardedAd = new RewardedAd(this,
                BuildConfig.ADD_UNIT_CODE);

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.i("ADS_LOADED", "Ads loaded success");
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    @Override
    public void onEditExercise(CreateExerciseRequestDTO exercise, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
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
    public void removeLogFromCache(int position) {
        mainActivityViewModel.removeLogFromCache(position);
    }

    @Override
    public void addLogToCache(LogDTO log) {
        mainActivityViewModel.addLogToCache(log);
    }

    public void stopPooling() {
        mainActivityViewModel
                .getDataFetchTimer()
                .cancel();
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

    public void setRewardedAd(RewardedAd rewardedAd) {
        this.rewardedAd = rewardedAd;
    }

    public RewardedAd getRewardedAd() {
        return rewardedAd;
    }

}
