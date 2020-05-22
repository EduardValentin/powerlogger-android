package com.example.powerlogger.ui.exercises.edit;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.MinifiedExerciseDTO;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.repositories.ExerciseRepository;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditExerciseViewModel extends ViewModel {

    private MinifiedExerciseDTO exerciseDTO;
    private ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();

    public MinifiedExerciseDTO getExerciseDTO() {
        return exerciseDTO;
    }

    public void submitExercise(Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.updateExercise(exerciseDTO.getId(), exerciseDTO, onSuccess, onError);
    }

    public void setExerciseDTO(MinifiedExerciseDTO exerciseDTO) {
        this.exerciseDTO = exerciseDTO;

        this.exerciseDTO.setGroupIds(this.exerciseDTO.getGroups()
                .stream().map(GroupDTO::getId).collect(Collectors.toList()));
    }
}
