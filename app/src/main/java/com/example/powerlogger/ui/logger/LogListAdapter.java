package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.powerlogger.R;

import java.util.ArrayList;

public class LogListAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Log> logList;
    private final int resourceLayout;

    public LogListAdapter(Context context, int resource, ArrayList<Log> logList) {
        this.context = context;
        this.resourceLayout = resource;
        this.logList = new ArrayList<>(logList);
    }

    @Override
    public int getCount() {
        return logList.size();
    }

    @Override
    public Object getItem(int i) {
        return logList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // TODO: move to utility class
    private float pxToDp(int px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    private float dpToPx(float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;
            LayoutInflater viewInflater = LayoutInflater.from(context);
            rowView = viewInflater.inflate(resourceLayout, null);
        // Layout inflater is used to populate a layout file with data
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Populate the row xml layout and add it to it's group
        // TODO: Check out recycle view method for smooth scroling
//        View rowView = layoutInflater.inflate(R.layout.fragment_add_log, viewGroup);
            Log currentLog = logList.get(i);
            TextView logName = rowView.findViewById(R.id.logNameTextField);
            LinearLayout horizontalLayout = rowView.findViewById(R.id.lrVerticalLogAmmountLL);
            TextView logAmmountDesc = rowView.findViewById(R.id.lrLogAmmountDesc);
            switch (currentLog.getType()) {
                case STRENGTH:
                    logAmmountDesc.setText("kg's:");
                    break;
                case HIIT:
                case SLOW_CARDIO:
                case MEDIUM_PACE_CARDIO:
                    logAmmountDesc.setText("minutes:");
                    break;

                    default:
                        logAmmountDesc.setText("ammount:");
            }

            EditText etext =rowView.findViewById(R.id.lrAmmountEtext);
            etext.setText(Integer.toString(currentLog.getAmmount()));

            logName.setText(currentLog.getName());
            Spinner logType = rowView.findViewById(R.id.logTypeSpinner);
            logType.setSelection(currentLog.getType().ordinal());

            return rowView;
    }

    public ArrayList<Log> getData() {
        return logList;
    }

    public boolean setData(ArrayList<Log> newData) {
        return logList.addAll(newData);
    }
}
