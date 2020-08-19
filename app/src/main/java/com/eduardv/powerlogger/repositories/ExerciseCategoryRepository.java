package com.eduardv.powerlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eduardv.powerlogger.APIClient;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.lib.ApiGenericCallback;
import com.eduardv.powerlogger.services.ExerciseCategoryDataService;
import com.eduardv.powerlogger.services.ExerciseDataService;
import com.eduardv.powerlogger.utils.APICallsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ExerciseCategoryRepository {
    private static ExerciseCategoryRepository INSTANCE;
    private MutableLiveData<List<ExerciseCategoryDTO>> categoriesCache;
    private ExerciseCategoryDataService exerciseCategoryDataService;
    private UserRepository userRepository;

    private ExerciseCategoryRepository() {
        this.categoriesCache = new MutableLiveData<>();
        this.categoriesCache.setValue(new ArrayList<>());
        this.userRepository = UserRepository.getInstance();
        this.exerciseCategoryDataService = APIClient.getRetrofitInstance().create(ExerciseCategoryDataService.class);
    }

    public static ExerciseCategoryRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExerciseCategoryRepository();
        }
        return INSTANCE;
    }

    public void getAllCategories(Consumer<List<ExerciseCategoryDTO>> onSuccess, Consumer<Throwable> onFail) {
        Consumer<List<ExerciseCategoryDTO>> consumerSuccess = categories -> {
            categoriesCache.setValue(categories);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(categories);
        };

        exerciseCategoryDataService.fetchAllCategories(userRepository.getToken()).enqueue(new ApiGenericCallback<>(consumerSuccess, onFail, "FETCH_CATEGORIES"));
    }

    public LiveData<List<ExerciseCategoryDTO>> getCategoriesCache() {
        return categoriesCache;
    }

    public Optional<ExerciseCategoryDTO> getCategoryFromString(String name) {
        return categoriesCache.getValue()
                .stream()
                .filter(exerciseCategoryDTO -> exerciseCategoryDTO.getIdentifierName().equals(name))
                .findFirst();
    }
}
