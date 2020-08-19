package com.eduardv.powerlogger.ui.exercises.edit;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.repositories.ExerciseRepository;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditExerciseViewModel extends ViewModel {

    private CreateExerciseRequestDTO exerciseDTO;
    private ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();

    public CreateExerciseRequestDTO getExerciseDTO() {
        return exerciseDTO;
    }

    public void submitExercise(Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.updateExercise(exerciseDTO.getId(), exerciseDTO, onSuccess, onError);
    }

    public void setExerciseDTO(CreateExerciseRequestDTO exerciseDTO) {
        this.exerciseDTO = exerciseDTO;

        this.exerciseDTO.setGroupIds(this.exerciseDTO.getGroups()
                .stream().map(GroupDTO::getId).collect(Collectors.toList()));
    }
}
