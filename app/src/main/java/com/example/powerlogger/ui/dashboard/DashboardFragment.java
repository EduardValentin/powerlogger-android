package com.example.powerlogger.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setView(this);

        Entry e1 = new Entry(1, 10);
        Entry e2 = new Entry(2, 30);
        Entry e3 = new Entry(3, 20);
        Entry e4 = new Entry(4, 60);
        Entry e5 = new Entry(5, 30);
        Entry e6 = new Entry(6, 20);
        Entry e7 = new Entry(7, 60);

        List<Entry> entries = Arrays.asList(e1, e2, e3, e4, e5, e6, e7);
        LineDataSet dataSet = new LineDataSet(entries, "Kcal per day");
        binding.lineChart.setData(new LineData(dataSet));
        Description lineChartDesc = new Description();
        lineChartDesc.setText("Calories burned this week");
        binding.lineChart.setDescription(lineChartDesc);
        binding.lineChart.setDrawGridBackground(false);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        int range = 10;
        int count = 5;

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            pieEntries.add(new PieEntry((float) ((Math.random() * range) + range / 5), 2));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Election Results");

        pieDataSet.setDrawIcons(false);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        pieDataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new PercentFormatter(binding.pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        binding.pieChart.setData(data);


        BarDataSet barDataSet = new BarDataSet(buildBarChartData(), "Top 10 exercises");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        binding.barChart.setData(barData);
        binding.barChart.invalidate();

        return binding.getRoot();
    }


    private List<BarEntry> buildBarChartData() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 3));
        entries.add(new BarEntry(1, 2));
        entries.add(new BarEntry(2, 6));
        entries.add(new BarEntry(4, 10));
        return entries;
    }
}