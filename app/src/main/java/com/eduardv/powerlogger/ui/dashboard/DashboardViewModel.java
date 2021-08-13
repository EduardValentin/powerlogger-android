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

    public DashboardViewModel() {
        userRepository.fetchUserStats(userStatsLive::setValue,
                t -> Log.e("ERROR_FETCH_USER_STATS", t.getMessage()));

        userRepository.fetchKcalLastWeek(kcalLastWeekLive::setValue,
                t -> Log.e("ERROR_FETCH_KCAL_LAST", t.getMessage()));

        userRepository.fetchTopExerciseTypes(topExerciseTypesLive::setValue,
                t -> Log.e("ERROR_FETCH_TOP_E_TYPES", t.getMessage()));

        userRepository.fetchTopGroups(topGroupsLive::setValue,
                t -> Log.e("ERROR_FETCH_TOP_GROUPS", t.getMessage()));

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