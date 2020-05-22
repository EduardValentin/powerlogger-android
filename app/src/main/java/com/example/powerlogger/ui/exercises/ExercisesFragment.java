package com.example.powerlogger.ui.exercises;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerlogger.MainActivityViewModel;
import com.example.powerlogger.MinifiedExerciseDTO;
import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentExercisesBinding;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;
import com.example.powerlogger.ui.exercises.edit.EditExerciseFragment;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

public class ExercisesFragment extends Fragment {

    private FragmentExercisesBinding bindingFragment;
    private MainActivityViewModel mainActivityViewModel;
    private ExercisesViewModel exercisesViewModel;
    private MutableLiveData<List<ExerciseDTO>> exerciseLiveData;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bindingFragment = DataBindingUtil.inflate(inflater, R.layout.fragment_exercises, container, false);
        bindingFragment.setView(this);

        View view = bindingFragment.getRoot();

        mainActivityViewModel = new ViewModelProvider(this.getActivity()).get(MainActivityViewModel.class);
        exercisesViewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);

        exerciseLiveData = mainActivityViewModel.getExerciseLiveData();

        exerciseLiveData.observe(getViewLifecycleOwner(), this::renderExercises);

        renderExercises(exerciseLiveData.getValue());

//        renderExercises(mainActivityViewModel.getExerciseLiveData().getValue());
        return view;
    }

    private void renderExercises(List<ExerciseDTO> exercises) {
        ExercisesAdapter adapter = new ExercisesAdapter(exercises, this::onExerciseClick);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        bindingFragment.exercisesList.setAdapter(adapter);
        bindingFragment.exercisesList.setHasFixedSize(false);
        bindingFragment.exercisesList.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(adapter);
        helper.attachToRecyclerView(bindingFragment.exercisesList);
    }

    private ItemTouchHelper createItemTouchHelper(RecyclerView.Adapter adapter) {
        return new ItemTouchHelper(
                new SwipeCallbackWithDeleteIcon(0, LEFT, adapter, this::deleteExercise, getContext()));
    }

    private void onExerciseClick(View view) {
        Integer position = (Integer) view.getTag();
        goToEditExercise(exerciseLiveData.getValue().get(position));
    }


//    private void renderExercises(List<ExerciseDTO> exercises) {
//        bindingFragment.exercisesExpandingList.removeAllViews();
//
//        exercises.forEach(exercise -> {
//            ExpandingItem item = bindingFragment.exercisesExpandingList
//                    .createNewItem(R.layout.exercise_expandable_layout);
//
//            TextView exerciseTextView = item.findViewById(R.id.exerciseTitle);
//            exerciseTextView.setText(exercise.getName());
//
//            item.createSubItems(1);
//            View subItemView = item.getSubItemView(0);
//
//            TextView categoryName = subItemView.findViewById(R.id.exerciseCategoryNameValue);
//            categoryName.setText(exercise.getCategory().getName());
//
//            TextView groupsList = subItemView.findViewById(R.id.exerciseGroupList);
//
//            String groupsString = exercise.getGroups() != null ?
//                    exercise.getGroups().stream()
//                            .map(GroupDTO::getName)
//                            .reduce((g1, g2) -> g1 + ", " + g2)
//                            .orElse("No workouts") : "No workouts";
//
//            groupsList.setText(groupsString);
//
//
//            Button editButton = subItemView.findViewById(R.id.exerciseEditButton);
//            editButton.setOnClickListener(v -> goToEditExercise(exercise));
//
//
//            Button deleteButton = subItemView.findViewById(R.id.exerciseDeleteButton);
//            deleteButton.setOnClickListener(v -> exercisesViewModel.deleteExercise(exercise.getId().toString(), this::onDeleteSuccess, this::onDeleteError));
//            item.collapse();
//        });
//    }

    public void onDeleteSuccess(Object o) {

    }

    public void onDeleteError(Throwable t) {

    }

    public void goToEditExercise(ExerciseDTO exerciseDTO) {
        Fragment fragment = new EditExerciseFragment();
        Bundle data = new Bundle();
        MinifiedExerciseDTO mini = new MinifiedExerciseDTO(exerciseDTO);

        data.putParcelable(ExercisesConstants.EXERCISE, mini);
        data.putParcelableArrayList(ExercisesConstants.GROUPS, (ArrayList<GroupDTO>) mainActivityViewModel.getGroupsLiveData().getValue());
        fragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getParentFragment().getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void deleteExercise(Integer position) {
        exercisesViewModel.deleteExercise(exerciseLiveData.getValue().get(position).getId(),
                this::onDeleteSuccess, this::onDeleteError);
    }
}
