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
        logDataService.postNewLog(log).enqueue(new Callback<LogDTO>() {
            @Override
            public void onResponse(Call<LogDTO> call, Response<LogDTO> response) {
                List<LogDTO> oldList = logsCahce.getValue();
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

    public void fetchLogs(LocalDate date) {
        logDataService.fetchAllLogs(date).enqueue(new Callback<List<LogDTO>>() {
            @Override
            public void onResponse(Call<List<LogDTO>> call, Response<List<LogDTO>> response) {
                logsCahce.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<LogDTO>> call, Throwable t) {

            }
        });
    }

    private LogRepository() {
        logDataService = APIClient.getRetrofitInstance().create(LogDataService.class);
        this.logsCahce.setValue(new ArrayList<>());
        this.logsCahce.getValue().add(new LogDTO("Exercitiu 1", LogType.FULL_BODY.toString(), "10"));
    }
}
