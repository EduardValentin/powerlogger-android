package com.eduardv.powerlogger.lib;

import android.util.Log;

import com.eduardv.powerlogger.utils.APICallsUtils;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiGenericCallback<T> implements Callback<T> {
    private Consumer<T> onSuccess;
    private Consumer<Throwable> onFail;
    private String tag;

    public ApiGenericCallback(Consumer<T> onSuccess, Consumer<Throwable> onFail, String tag) {
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.tag = tag;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            // Handle success
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(response.body());
        } else {
            // Handle status 4xx or 5xx
            APICallsUtils.getHandlerOrDefault(onFail).accept(new Exception("Unsuccessful api call. Tag: " + tag + ". Message: " + response.message()));
            Log.e(tag, "API call not successful. Status: " + response.code()
                    + ". Message:" + response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        APICallsUtils.getHandlerOrDefault(onFail).accept(t);
        Log.e(tag, "Error on api call. Error: " + t.getMessage());
    }
}
