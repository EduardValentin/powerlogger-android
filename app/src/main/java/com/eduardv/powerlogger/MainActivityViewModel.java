package com.eduardv.powerlogger;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.model.ExerciseCategory;
import com.eduardv.powerlogger.repositories.ExerciseCategoryRepository;
import com.eduardv.powerlogger.repositories.GroupRepository;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.repositories.LogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class MainActivityViewModel extends ViewModel {

    public static final int SECONDS = 1000;
    public static final int MINUTES = SECONDS * 60;

    private MutableLiveData<List<GroupDTO>> groupsLiveData;
    private MutableLiveData<List<ExerciseDTO>> exerciseLiveData;
    private MutableLiveData<List<ExerciseCategoryDTO>> exerciseCategoriesLiveData;

    private final MutableLiveData<Integer> chartRewards;
    private final GroupRepository groupRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private final LogRepository logRepository;
    private final Timer rewardCleanTimer;

    public MainActivityViewModel() {

        chartRewards = new MutableLiveData<>();
        chartRewards.setValue(0);

        groupsLiveData = new MutableLiveData<>();
        groupsLiveData.setValue(new ArrayList<>());

        exerciseLiveData = new MutableLiveData<>();
        exerciseLiveData.setValue(new ArrayList<>());

        exerciseCategoriesLiveData = new MutableLiveData<>();
        exerciseCategoriesLiveData.setValue(new ArrayList<>());

        exerciseRepository = ExerciseRepository.getInstance();
        groupRepository = GroupRepository.getInstance();
        logRepository = LogRepository.getInstance();
        exerciseCategoryRepository = ExerciseCategoryRepository.getInstance();

        exerciseCategoryRepository.getCategoriesCache().observeForever(exerciseCategoriesLiveData::setValue);
        exerciseRepository.getExerciseCache().observeForever(exerciseLiveData::setValue);
        groupRepository.getGroupCache().observeForever(groupsLiveData::setValue);

        fetchExercises();
        fetchGroups();
        fetchCategories();

        rewardCleanTimer = new Timer();
        rewardCleanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                chartRewards.postValue(0);
            }
        }, 30 * MINUTES, 30 * MINUTES);

    }

    public MutableLiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }

    public void sendBatchLogs(List<LogDTO> logsToSend) {
        logRepository.sendBatchLogs(logsToSend, null, null);
    }

    public void removeGroup(String groupId, Consumer<Void> onSuccess, Consumer<Throwable> onError) {
        groupRepository.removeGroup(groupId, onSuccess, onError);
    }

    public MutableLiveData<List<ExerciseDTO>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public LiveData<List<ExerciseCategoryDTO>> getExerciseCategoriesLive() {
        return exerciseCategoriesLiveData;
    }

    private void fetchExercises() {
        exerciseRepository.fetchExercises(
                null,
                this::onScheduledCallError);
    }

    private void onScheduledCallError(Throwable t) {
        Log.e("SCHEDULED_CALL_ERROR", t.getMessage());
    }

    private void fetchGroups() {
        groupRepository.fetchGroups(
                null,
                this::onScheduledCallError);
    }


    private void fetchCategories() {
        exerciseCategoryRepository.getAllCategories(
                null,
                this::onScheduledCallError);
    }

    public void removeLogFromCache(int position) {
        logRepository.removeLogFromCache(position);
    }

    public void addLogToCache(LogDTO log) {
        logRepository.addLogToCache(log);
    }

    public MutableLiveData<Integer> getChartRewards() {
        return chartRewards;
    }
}
