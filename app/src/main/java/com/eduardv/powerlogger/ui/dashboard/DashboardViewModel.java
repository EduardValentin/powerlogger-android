package com.eduardv.powerlogger.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.charts.ChartResponseDTO;
import com.eduardv.powerlogger.dto.charts.UserStatsDTO;
import com.eduardv.powerlogger.repositories.UserRepository;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<UserStatsDTO> userStatsLive = new MutableLiveData<>();
    private MutableLiveData<ChartResponseDTO> kcalLastWeekLive = new MutableLiveData<>();
    private MutableLiveData<ChartResponseDTO> topExerciseTypesLive = new MutableLiveData<>();
    private MutableLiveData<ChartResponseDTO> topGroupsLive = new MutableLiveData<>();
    private UserRepository userRepository = UserRepository.getInstance();
    private final Timer timer;

    public DashboardViewModel() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("Scheduled fetch chart data", "Started fetching data");
                userRepository.fetchUserStats(userStatsLive::setValue,
                        t -> Log.e("ERROR_FETCH_USER_STATS", t.getMessage()));

                userRepository.fetchKcalLastWeek(kcalLastWeekLive::setValue,
                        t -> Log.e("ERROR_FETCH_KCAL_LAST_WEEK", t.getMessage()));

                userRepository.fetchTopExerciseTypes(topExerciseTypesLive::setValue,
                        t -> Log.e("ERROR_FETCH_TOP_E_TYPES", t.getMessage()));

                userRepository.fetchTopGroups(topGroupsLive::setValue,
                        t -> Log.e("ERROR_FETCH_TOP_GROUPS_WEEK", t.getMessage()));
            }
        }, 0, 1000L * 30);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer.cancel();
    }

    LiveData<UserStatsDTO> getUserStatsLive() {
        return userStatsLive;
    }

    LiveData<ChartResponseDTO> getKcalLastWeekLive() {
        return kcalLastWeekLive;
    }

    LiveData<ChartResponseDTO> getTopExerciseTypesLive() {
        return topExerciseTypesLive;
    }

    LiveData<ChartResponseDTO> getTopGroupsLive() {
        return topGroupsLive;
    }
}