package com.eduardv.powerlogger.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.eduardv.powerlogger.APIClient;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.lib.ApiGenericCallback;
import com.eduardv.powerlogger.services.LogDataService;
import com.eduardv.powerlogger.utils.APICallsUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogRepository {
    private static LogRepository ourInstance;
    private LogDataService logDataService;
    private MutableLiveData<List<LogDTO>> logsCahce = new MutableLiveData<>();
    private UserRepository userRepository = UserRepository.getInstance();

    public static LogRepository getInstance() {
        if (ourInstance == null) {
            ourInstance = new LogRepository();
        }
        return ourInstance;
    }

    public MutableLiveData<List<LogDTO>> getLogsCache() {
        return logsCahce;
    }

    public void addLog(LogDTO log, ExerciseDTO exerciseDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.postNewLog(userRepository.getUser().getUsername(), userRepository.getToken(), exerciseDTO.getId(), log)
                .enqueue(new Callback<LogDTO>() {
                    @Override
                    public void onResponse(Call<LogDTO> call, Response<LogDTO> response) {
                        List<LogDTO> oldList = logsCahce.getValue();
                        // TODO: see error handling
                        if (response.body() == null) {
                            APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                            return;
                        }

                        oldList.add(response.body());
                        logsCahce.setValue(oldList);
                        APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                    }

                    @Override
                    public void onFailure(Call<LogDTO> call, Throwable t) {
                        APICallsUtils.getHandlerOrDefault(handleError).accept(t);
                    }
                });
    }

    public void fetchLogs(LocalDate date, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.fetchAllLogs(userRepository.getUser().getUsername(), userRepository.getToken(), date).enqueue(new Callback<List<LogDTO>>() {
            @Override
            public void onResponse(Call<List<LogDTO>> call, Response<List<LogDTO>> response) {
                if (response.code() == 200) {
                    logsCahce.setValue(response.body());
                    APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                } else {
                    Log.e("FETCH LOGS ERROR", "Error while fetching logs with date: " + date + " for user: " + userRepository.getToken());
                    logsCahce.setValue(new ArrayList<>());
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<LogDTO>> call, Throwable t) {
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
    }

    public void updateLog(LogDTO logDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.updateLog(userRepository.getUser().getUsername(), userRepository.getToken(), logDTO.getId(), logDTO).enqueue(new Callback<LogDTO>() {
            @Override
            public void onResponse(Call<LogDTO> call, Response<LogDTO> response) {

                if (response.body() == null) {
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                    return;
                }

                List<LogDTO> listToUpdate = logsCahce.getValue();
                LogDTO logToUpdate = listToUpdate.stream().filter(log -> log.getId().equals(response.body().getId())).findFirst().get();
                listToUpdate.remove(logToUpdate);
                listToUpdate.add(response.body());
                logsCahce.setValue(listToUpdate);

                APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
            }

            @Override
            public void onFailure(Call<LogDTO> call, Throwable t) {
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
    }

    public void getLogCalories(String category, LogDTO logDTO, Consumer<LogDTO> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.getCaloriesForLog(userRepository.getUser().getUsername(), userRepository.getToken(), category, logDTO.getIntensity(), logDTO.getIntensityType())
                .enqueue(new ApiGenericCallback<>(handleSuccess, handleError, "LOG_COMPUTE_CALORIES"));
    }

    public void sendBatchLogs(List<LogDTO> logs, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        Consumer<List<LogDTO>> onResponseSuccess = responseBody -> {
            List<LogDTO> cacheLogs = logsCahce.getValue();
            assert cacheLogs != null;
            cacheLogs.addAll(responseBody);

            logsCahce.setValue(cacheLogs);

            APICallsUtils.getHandlerOrDefault(handleSuccess).accept(responseBody);
        };

        logDataService.sendBatchLogs(userRepository.getUser().getUsername(), userRepository.getToken(), logs)
                .enqueue(new ApiGenericCallback<List<LogDTO>>(onResponseSuccess, handleError, "BATCH_LOGS"));
    }

    public void removeLog(String logId, Optional<Runnable> handleSuccess, Consumer<Throwable> handleError) {
        Consumer<Void> onResponseSuccess = resp -> {
            List<LogDTO> cache = logsCahce.getValue();
            assert cache != null;
            cache.removeIf(log -> log.getId().equals(logId));
            logsCahce.setValue(cache);

            handleSuccess.ifPresent(Runnable::run);
        };

        logDataService.delete(userRepository.getUser().getUsername(), userRepository.getToken(), logId)
                .enqueue(new ApiGenericCallback(onResponseSuccess, handleError, "LOG_DELETE"));
    }

    private LogRepository() {
        this.logsCahce.setValue(new ArrayList<>());
        logDataService = APIClient.getRetrofitInstance().create(LogDataService.class);
    }

    public void addLogToCache(LogDTO log) {
        List<LogDTO> logs = logsCahce.getValue();
        logs.add(log);
        logsCahce.setValue(logs);
    }

    public void removeLogFromCache(int position) {
        List<LogDTO> logs = logsCahce.getValue();
        logs.remove(position);
        logsCahce.setValue(logs);
    }
}
