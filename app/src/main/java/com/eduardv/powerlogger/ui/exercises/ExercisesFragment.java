package com.eduardv.powerlogger.ui.exercises;

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

import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentExercisesBinding;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.lists.RecyclerItemTouchHelperListener;
import com.eduardv.powerlogger.lib.lists.SwipeCallbackWithDeleteIcon;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.ui.exercises.create.CreateExerciseFragment;
import com.eduardv.powerlogger.ui.exercises.edit.EditExerciseFragment;
import com.eduardv.powerlogger.ui.groups.GroupsViewHolder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_TIMEOUT;

public class ExercisesFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private FragmentExercisesBinding bindingFragment;
    private MainActivityViewModel mainActivityViewModel;
    private ExercisesViewModel exercisesViewModel;
    private MutableLiveData<List<ExerciseDTO>> exerciseLiveData;
    private ExercisesAdapter exercisesAdapter;
    private List<ExerciseDTO> exercises;
    private Snackbar snackbar;

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

        return view;
    }

    private void renderExercises(List<ExerciseDTO> exercises) {

        if (exercises.isEmpty()) {
            bindingFragment.noExercisesCreated.setVisibility(View.VISIBLE);
            bindingFragment.exercisesList.setVisibility(View.INVISIBLE);
            return;
        }

        this.exercises = new ArrayList<>();
        this.exercises.add(new ExerciseDTO());
        this.exercises.addAll(exercises);

        this.exercisesAdapter = new ExercisesAdapter(this.exercises, this::onExerciseClick);
        this.exercisesAdapter.setCategories(mainActivityViewModel.getExerciseCategoriesLive().getValue());
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());

        bindingFragment.exercisesList.setAdapter(exercisesAdapter);
        bindingFragment.exercisesList.setHasFixedSize(false);
        bindingFragment.exercisesList.setLayoutManager(layoutManager);

        ItemTouchHelper helper = createItemTouchHelper(exercisesAdapter);
        helper.attachToRecyclerView(bindingFragment.exercisesList);
    }

    private ItemTouchHelper createItemTouchHelper(RecyclerView.Adapter adapter) {
        return new ItemTouchHelper(
                new SwipeCallbackWithDeleteIcon(0, LEFT, this));
    }

    private void onExerciseClick(View view) {
        Integer position = (Integer) view.getTag();
        goToEditExercise(exercises.get(position));
    }

    public void onAddNewExerciseClick(View v) {
        Fragment createExerciseFragment = new CreateExerciseFragment();
        FragmentTransaction fragmentTransaction = getParentFragment().getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, createExerciseFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void onDeleteSuccess(Object o) {

    }

    private void onDeleteError(Throwable t) {

    }

    private void goToEditExercise(ExerciseDTO exerciseDTO) {
        Fragment fragment = new EditExerciseFragment();
        Bundle data = new Bundle();
        CreateExerciseRequestDTO mini = new CreateExerciseRequestDTO(exerciseDTO);

        data.putParcelable(ExercisesConstants.EXERCISE, mini);
        data.putParcelableArrayList(ExercisesConstants.GROUPS, (ArrayList<GroupDTO>) mainActivityViewModel.getGroupsLiveData().getValue());
        fragment.setArguments(data);

        FragmentTransaction fragmentTransaction = getParentFragment().getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    private void deleteExercise(ExerciseDTO exercise) {
        exercisesViewModel.deleteExercise(exercise.getId(),
                this::onDeleteSuccess, this::onDeleteError);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ExerciseViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();

            // get the removed item name to display it in snack bar
            String name = exercises.get(deletedIndex).getName();

            // backup of removed item for undo purpose
            final ExerciseDTO deletedItem = exercises.get(deletedIndex);

            // remove the item from recycler view
            exercisesViewModel.removeExerciseFromCache(deletedIndex - 1);
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            // showing snack bar with Undo option
            snackbar = Snackbar
                    .make(bindingFragment.getRoot(), "Exercise " + name + " removed", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> {
                // undo is selected, restore the deleted item
                exercisesViewModel.addExerciseToCache(deletedItem);
            });

            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_MANUAL) {
                        deleteExercise(deletedItem);
                    }
                }
            });

            snackbar.show();
        }
    }
}
