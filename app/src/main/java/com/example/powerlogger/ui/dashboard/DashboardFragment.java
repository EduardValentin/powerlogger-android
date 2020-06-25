package com.example.powerlogger.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.powerlogger.R;
import com.example.powerlogger.databinding.FragmentDashboardBinding;
import com.example.powerlogger.dto.UserActiveStatsDTO;
import com.github.mikephil.charting.animation.Easing;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static android.widget.TextView.BufferType.SPANNABLE;

public class DashboardFragment extends Fragment {
    private final static String PLACEHOLDER = "<plc>";

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private SpannableStringBuilder activeThanPeople = new SpannableStringBuilder("You are more active than " + PLACEHOLDER + " of people");
    private SpannableStringBuilder activeThanWeek = new SpannableStringBuilder("You are " + PLACEHOLDER + " " + PLACEHOLDER + " active this week than last week");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setView(this);

        UserActiveStatsDTO hcUserActiveStatsDTO = new UserActiveStatsDTO();
        hcUserActiveStatsDTO.setPeoplePercent("31%");
        hcUserActiveStatsDTO.setWeekAdverb("more");
        hcUserActiveStatsDTO.setWeekPercent("77%");

        setUserActiveStats(hcUserActiveStatsDTO);

        binding.activeThanPercent.setText(activeThanPeople, SPANNABLE);
        binding.activeThanWeek.setText(activeThanWeek, SPANNABLE);

        LineDataSet dataSet = getLineDataSet();
        Description lineChartDesc = new Description();
        lineChartDesc.setText("Calories burned this week");

        binding.lineChart.setData(new LineData(dataSet));
        binding.lineChart.setDescription(lineChartDesc);
        binding.lineChart.setDrawGridBackground(false);
        binding.lineChart.animateY(1400, Easing.EaseInOutQuad);

        PieDataSet pieDataSet = getPieDataSet();
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);
        binding.pieChart.setData(pieData);

        binding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        binding.pieChart.setHighlightPerTapEnabled(true);
        binding.pieChart.setDrawEntryLabels(false);
        Description desc = new Description();
        desc.setText("Count of logs of this type");
        binding.pieChart.setDescription(desc);
        pieDataSet.setDrawIcons(false);
        binding.pieChart.setEntryLabelTextSize(30);


        BarDataSet barDataSet = new BarDataSet(buildBarChartData(), "Exercise name");
        barDataSet.setColors(makeColors(10));
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        binding.barChart.setData(barData);
        binding.barChart.invalidate();
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setDrawMarkers(true);
        binding.barChart.getLegend().setEnabled(true);
        binding.barChart.animateY(1400, Easing.EaseInOutQuad);
        Description barDesc = new Description();
        barDesc.setText("Number of times you've done the exercise");
        binding.barChart.setDescription(barDesc);
        return binding.getRoot();
    }

    private void setUserActiveStats(UserActiveStatsDTO userActiveStatsDTO) {
        final String activeThanPeopleStr = activeThanPeople.toString();
        final int startPeoplePercent = activeThanPeopleStr.indexOf(PLACEHOLDER);
        final int endPeoplePercent = startPeoplePercent + PLACEHOLDER.length();
        final String peoplePercent = userActiveStatsDTO.getPeoplePercent();

        activeThanPeople.replace(startPeoplePercent, endPeoplePercent, peoplePercent);
        activeThanPeople.setSpan(new ForegroundColorSpan(Color.BLUE), startPeoplePercent, startPeoplePercent + peoplePercent.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        String activeThanWeekStr = activeThanWeek.toString();
        final int startWeekPercent = activeThanWeekStr.indexOf(PLACEHOLDER);
        final int endWeekPercent = startWeekPercent + PLACEHOLDER.length();
        final String weekPercent = userActiveStatsDTO.getWeekPercent();

        activeThanWeek.replace(startWeekPercent, endWeekPercent, weekPercent);
        activeThanWeek.setSpan(new ForegroundColorSpan(Color.BLUE), startWeekPercent, startWeekPercent + weekPercent.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        activeThanWeekStr = activeThanWeek.toString();
        final int startWeekAdverb = activeThanWeekStr.indexOf(PLACEHOLDER);
        final int endWeekAdverb = startWeekAdverb + PLACEHOLDER.length();
        final String weekAdverb = userActiveStatsDTO.getWeekAdverb();

        activeThanWeek.replace(startWeekAdverb, endWeekAdverb, weekAdverb);
        activeThanWeek.setSpan(new ForegroundColorSpan(Color.BLUE), startWeekAdverb, startWeekAdverb + weekAdverb.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


    }

    public PieDataSet getPieDataSet() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        int range = 10;
        int count = 5;

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            pieEntries.add(new PieEntry((float) ((Math.random() * range) + range / 5), "HIIT"));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Exercise types");

        // add a lot of colors

        pieDataSet.setColors(makeColors(5));

        return pieDataSet;
    }

    public void onShareClick(View _) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "This week burned " + 521 + " kcal. Come and race me. Start logging your activity on powerlogger app.");
        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public LineDataSet getLineDataSet() {
        Entry e1 = new Entry(1, 10);
        Entry e2 = new Entry(2, 30);
        Entry e3 = new Entry(3, 20);
        Entry e4 = new Entry(4, 60);
        Entry e5 = new Entry(5, 30);
        Entry e6 = new Entry(6, 20);
        Entry e7 = new Entry(7, 60);

        List<Entry> entries = Arrays.asList(e1, e2, e3, e4, e5, e6, e7);
        LineDataSet dataSet = new LineDataSet(entries, "Kcal per day");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_gradient);
        dataSet.setFillDrawable(drawable);

        return dataSet;
    }

    private List<Integer> makeColors(int size) {
        HashSet<Integer> colorsPresent = new HashSet<>();
        final Pair<Integer, Integer> RANGE = new Pair(20, 200);

        while (colorsPresent.size() < size) {
            int R = getIntInRange(RANGE.first, RANGE.second);
            int G = getIntInRange(RANGE.first, RANGE.second);
            int B = getIntInRange(RANGE.first, RANGE.second);
            Integer randomColor = Color.rgb(R, G, B);

            if (!colorsPresent.contains(randomColor)) {
                colorsPresent.add(randomColor);
            }
        }

        return new ArrayList<>(colorsPresent);
    }

    private int getIntInRange(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }

    private List<BarEntry> buildBarChartData() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 3, "Chest press"));
        entries.add(new BarEntry(1, 2, "Abs Upper"));
        entries.add(new BarEntry(2, 6, "Legs"));
        entries.add(new BarEntry(3, 10, "Neck"));
        entries.add(new BarEntry(4, 10, "Legs"));
        entries.add(new BarEntry(5, 32, "Legs"));
        entries.add(new BarEntry(6, 10, "Legs"));
        entries.add(new BarEntry(7, 55, "Legs"));
        entries.add(new BarEntry(8, 10, "Legs"));
        entries.add(new BarEntry(9, 1, "Legs"));

        return entries;
    }
}