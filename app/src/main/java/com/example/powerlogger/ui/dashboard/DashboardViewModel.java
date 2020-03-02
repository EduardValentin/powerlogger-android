package com.example.powerlogger.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.CaloriesInfoDTO;
import com.example.powerlogger.repositories.CaloriesRepository;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<CaloriesInfoDTO> calorieInfo = new MutableLiveData<>();
    private CaloriesRepository caloriesRepository = CaloriesRepository.getInstance();

    public DashboardViewModel() {
//        caloriesRepository.getCalorieCache().observeForever(caloriesInfoDTO -> calorieInfo.setValue(caloriesInfoDTO));

    }

    public void fetchInfo() {
        caloriesRepository.fetchCalorieInfo();
    }

    public MutableLiveData<CaloriesInfoDTO> getCalorieInfo() {
        return calorieInfo;
    }
}