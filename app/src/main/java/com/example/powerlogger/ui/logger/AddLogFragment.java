package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.powerlogger.R;

import java.util.ArrayList;

public class AddLogFragment extends Fragment {
    ListView logsListView;
    ArrayList<Log> logsArrayList;
    LogListAdapter logListAdapter;

    private OnFragmentInteractionListener mListener;

    public AddLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_log, container, false);
        logsListView = view.findViewById(R.id.logsListView);
        logsArrayList = new ArrayList<>();

        // Mock data
        logsArrayList.add(new Log("Sarit cu coarda", LogType.HIIT, 45));
        logsArrayList.add(new Log("Sprint", LogType.HIIT, 15));
        logsArrayList.add(new Log("Piept", LogType.STRENGTH, 50));

        logListAdapter = new LogListAdapter(getActivity(), R.layout.log_row, logsArrayList);
        logsListView.setAdapter(logListAdapter);

        Button addExerciseToLogButton = view.findViewById(R.id.addExerciseToLogBtn);
        addExerciseToLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logsArrayList.add(new Log("", LogType.HIIT, 0));
                logListAdapter.getData().clear();
                boolean r = logListAdapter.setData(logsArrayList);

                logListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
