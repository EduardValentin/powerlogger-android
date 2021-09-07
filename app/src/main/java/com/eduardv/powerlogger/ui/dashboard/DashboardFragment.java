package com.eduardv.powerlogger.ui.dashboard;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.eduardv.powerlogger.MainActivity;
import com.eduardv.powerlogger.MainActivityViewModel;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.databinding.FragmentDashboardBinding;
import com.eduardv.powerlogger.dto.charts.AxisPoint;
import com.eduardv.powerlogger.dto.charts.ChartResponseDTO;
import com.eduardv.powerlogger.dto.charts.UserStatsDTO;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import static android.view.View.*;
import static android.view.View.INVISIBLE;
import static android.widget.TextView.BufferType.SPANNABLE;

public class DashboardFragment extends Fragment {
    private final static String PLACEHOLDER = "$";

    private DashboardViewModel dashboardViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private FragmentDashboardBinding binding;
    private SpannableStringBuilder activeThanPeople = new SpannableStringBuilder("");
    private SpannableStringBuilder activeThanWeek = new SpannableStringBuilder("");
    private List<Integer> barChartColors;
    private List<Integer> pieChartColors;

    private final ValueFormatter formatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(((int) value));
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setView(this);

        this.barChartColors = makeColors(10);
        this.pieChartColors = makeColors(10);

        final int colorOnBackground = getColorFromAttribute(R.attr.colorOnBackground);
        final int backgroundColor = getColorFromAttribute(R.attr.backgroundColor);

        binding.activeThanPercent.setText(activeThanPeople, SPANNABLE);
        binding.activeThanWeek.setText(activeThanWeek, SPANNABLE);
        binding.lineChart.setDrawGridBackground(false);
        binding.lineChart.animateY(1400, Easing.EaseInOutQuad);
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getAxisLeft().setAxisMinimum(0);
        binding.lineChart.getAxisRight().setAxisMinimum(0);
        binding.lineChart.getXAxis().setTextColor(colorOnBackground);
        binding.lineChart.getAxisLeft().setTextColor(colorOnBackground);
        binding.lineChart.getAxisRight().setTextColor(colorOnBackground);

        binding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        binding.pieChart.setHighlightPerTapEnabled(true);
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setEntryLabelTextSize(30);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setNoDataText(getString(R.string.no_piechart_data));
        binding.pieChart.setEntryLabelColor(colorOnBackground);
        binding.pieChart.setHoleColor(backgroundColor);
        binding.pieChart.getLegend().setTextColor(colorOnBackground);

        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setDrawMarkers(true);
        binding.barChart.getLegend().setEnabled(true);
        binding.barChart.animateY(1400, Easing.EaseInOutQuad);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setNoDataText(getString(R.string.no_barchart_data));

        binding.barChart.getXAxis().setTextColor(colorOnBackground);
        binding.barChart.getAxisLeft().setTextColor(colorOnBackground);
        binding.barChart.getAxisRight().setTextColor(colorOnBackground);

        dashboardViewModel.getUserStatsLive().observe(getViewLifecycleOwner(), this::setUserActiveStats);
        dashboardViewModel.getKcalLastWeekLive().observe(getViewLifecycleOwner(), this::updateLastWeekKcalData);
        dashboardViewModel.getTopExerciseTypesLive().observe(getViewLifecycleOwner(), this::updateTopExercisesTypeData);
        dashboardViewModel.getTopGroupsLive().observe(getViewLifecycleOwner(), this::updateTopGroupsData);

        mainActivityViewModel.getChartRewards().observe(getViewLifecycleOwner(), rewards -> {
//            if (rewards > 0) {
//                binding.blurredBarChartLayout.setVisibility(INVISIBLE);
//                binding.blurredPieChartLayout.setVisibility(INVISIBLE);
//                binding.barChart.setVisibility(VISIBLE);
//                binding.pieChart.setVisibility(VISIBLE);
//            } else {
//                binding.blurredBarChartLayout.setVisibility(VISIBLE);
//                binding.blurredPieChartLayout.setVisibility(VISIBLE);
//                binding.barChart.setVisibility(INVISIBLE);
//                binding.pieChart.setVisibility(INVISIBLE);
//            }

            binding.blurredBarChartLayout.setVisibility(INVISIBLE);
            binding.blurredPieChartLayout.setVisibility(INVISIBLE);
            binding.barChart.setVisibility(VISIBLE);
            binding.pieChart.setVisibility(VISIBLE);
        });


        return binding.getRoot();
    }


    public void showAd(View v) {
        MainActivity mainActivity = (MainActivity) getActivity();
        RewardedAd rewardedAd = mainActivity.getRewardedAd();

        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    mainActivity.setRewardedAd(mainActivity.createAndLoadRewardedAds());
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    mainActivityViewModel.getChartRewards().setValue(reward.getAmount());
                }
            };
            rewardedAd.show(mainActivity, adCallback);
        } else {
            Log.d("ADS", "The rewarded ad wasn't loaded yet.");
        }
    }


    private void updateTopGroupsData(ChartResponseDTO chartResponseDTO) {
        List<AxisPoint> axisPoints = chartResponseDTO.getPoints();

        if (axisPoints.isEmpty()) {
            return;
        }

        BarData barData = new BarData();

        int i = 0;
        for (AxisPoint axisPoint : axisPoints) {
            float val = Float.parseFloat(axisPoint.getYpoint());

            BarDataSet barDataSet = new BarDataSet(Collections.singletonList(new BarEntry(i, val)), axisPoint.getXpoint());
            barDataSet.setValueFormatter(formatter);
            barDataSet.setColors(barChartColors.get(i));
            barData.addDataSet(barDataSet);

            i++;
        }

        binding.barChart.getLegend().setWordWrapEnabled(true);
        binding.barChart.setData(barData);
    }

    private void updateTopExercisesTypeData(ChartResponseDTO chartResponseDTO) {
        List<AxisPoint> axisPoints = chartResponseDTO.getPoints();

        if (axisPoints.isEmpty()) {
            return;
        }

        List<PieEntry> pieEntries = axisPoints.stream()
                .map(axisPoint -> new PieEntry(Float.parseFloat(axisPoint.getYpoint()), axisPoint.getXpoint()))
                .collect(Collectors.toList());

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

        pieDataSet.setValueFormatter(formatter);

        // add a lot of colors
        pieDataSet.setColors(pieChartColors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);
        pieDataSet.setDrawIcons(false);

        binding.pieChart.getLegend().setWordWrapEnabled(true);
        binding.pieChart.invalidate();
        binding.pieChart.setData(pieData);

    }

    private void updateLastWeekKcalData(ChartResponseDTO chartResponseDTO) {
        List<AxisPoint> axis = chartResponseDTO.getPoints();
        List<Entry> entries = new ArrayList<>();

        for (AxisPoint p : axis) {
            entries.add(new Entry(entries.size(), Float.parseFloat(p.getYpoint())));
        }

        LineDataSet dataSet = new LineDataSet(entries, "calories / day");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#97be7b"));
        dataSet.setColors(Color.parseColor("#97be7b"));
        dataSet.setCircleColors(Color.parseColor("#82c5b2"));

        binding.lineChart.invalidate();
        binding.lineChart.setData(new LineData(dataSet));

        String[] labels = axis.stream().map(axisPoint -> {
            LocalDate ld = LocalDate.parse(axisPoint.getXpoint());
            return ld.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }).toArray(String[]::new);

        setXAxisLabels(binding.lineChart, labels);
    }

    private void setXAxisLabels(Chart chart, String... values) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
    }

    private void setUserActiveStats(UserStatsDTO userStatsDTO) {

        String activeThanPeopleString = userStatsDTO.getAllTimePerformanceMessage();
        activeThanPeople = new SpannableStringBuilder(userStatsDTO.getAllTimePerformanceMessage());

        Pair<Integer, Integer> activeThanPeoplePair = findNextTwoPlaceholders(activeThanPeopleString);
        setColoredSpanToPair(activeThanPeoplePair, activeThanPeople);

        binding.activeThanPercent.setText(activeThanPeople, SPANNABLE);

        String activeWeekString = userStatsDTO.getWeekPerformanceMessage();
        activeThanWeek = new SpannableStringBuilder(activeWeekString);
        Pair<Integer, Integer> weekPair = findNextTwoPlaceholders(activeWeekString);
        setColoredSpanToPair(weekPair, activeThanWeek);

        binding.activeThanWeek.setText(activeThanWeek, SPANNABLE);
    }

    private void setColoredSpanToPair(Pair<Integer, Integer> pair, SpannableStringBuilder span) {
        if (pair.first != -1 && pair.second != -1) {
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#97be7b")), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            span.replace(pair.first, pair.first + 1, "");
            span.replace(pair.second - 1, pair.second, "");
        }
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

    private void setChartDescription(Chart chart, String description) {
        Description chartDescription = new Description();
        chartDescription.setText(description);
        chart.setDescription(chartDescription);
    }

    private int getIntInRange(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }

    private Pair<Integer, Integer> findNextTwoPlaceholders(String str) {
        int first = str.indexOf(PLACEHOLDER);
        int second = str.indexOf(PLACEHOLDER, first + 1);
        return new Pair<>(first, second);
    }

    private int getColorFromAttribute(int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}