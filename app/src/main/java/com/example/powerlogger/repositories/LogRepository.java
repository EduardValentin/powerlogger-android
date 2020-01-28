package com.example.powerlogger.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.services.LogDataService;
import com.example.powerlogger.ui.logger.LogType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        if(ourInstance == null) {
            ourInstance = new LogRepository();
        }
        return ourInstance;
    }

    public  MutableLiveData<List<LogDTO>> getLogsCahce() {
        return logsCahce;
    }

    public void addLog(LogDTO log, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.postNewLog(userRepository.getToken(), log).enqueue(new Callback<LogDTO>() {
            @Override
            public void onResponse(Call<LogDTO> call, Response<LogDTO> response) {
                List<LogDTO> oldList = logsCahce.getValue();

                if(response.body() == null) {
                    return;
                }

                oldList.add(response.body());

                logsCahce.setValue(oldList);

                if(handleSuccess != null) {
                    handleSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<LogDTO> call, Throwable t) {
                if (handleError != null){
                    handleError.accept(t);
                }
            }
        });
    }

    public void fetchLogs(String date) {
        logDataService.fetchAllLogs(userRepository.getToken(), date).enqueue(new Callback<List<LogDTO>>() {
            @Override
            public void onResponse(Call<List<LogDTO>> call, Response<List<LogDTO>> response) {
                logsCahce.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<LogDTO>> call, Throwable t) {

            }
        });
    }

    public void updateLog(LogDTO logDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logDataService.updateLog(userRepository.getToken(), logDTO.getId(), logDTO).enqueue(new Callback<LogDTO>() {
            @Override
            public void onResponse(Call<LogDTO> call, Response<LogDTO> response) {

                if(response.body() == null) {
                    return;
                }

                List<LogDTO> listToUpdate = logsCahce.getValue();
                LogDTO logToUpdate = listToUpdate.stream().filter(log -> log.getId().equalsIgnoreCase(response.body().getId())).findFirst().get();
                listToUpdate.remove(logToUpdate);
                listToUpdate.add(response.body());
                logsCahce.setValue(listToUpdate);

                if(handleSuccess != null) {
                    handleSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<LogDTO> call, Throwable t) {
                if (handleError != null){
                    handleError.accept(t);
                }
            }
        });
    }

    private LogRepository() {
        this.logsCahce.setValue(new ArrayList<>());
        logDataService = APIClient.getRetrofitInstance().create(LogDataService.class);

    }
}
