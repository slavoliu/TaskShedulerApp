package com.example.taskscheduler;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private PieChart pieChart;
    private DataBaseHelper myDB;
    private Button btnSelectDate;
    private TextView sleepTimeTextView, evaluationTextView, workTimeTextView, personalTimeTextView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        sleepTimeTextView = view.findViewById(R.id.sleepTimeTextView);
        evaluationTextView = view.findViewById(R.id.evaluationTextView);
        workTimeTextView = view.findViewById(R.id.workTimeTextView);
        personalTimeTextView = view.findViewById(R.id.personalTimeTextView);
        myDB = new DataBaseHelper(getContext());
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Set button text to today's date
        btnSelectDate.setText(dateFormat.format(calendar.getTime()));

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initial setup for today's date
        setupPieChart(dateFormat.format(calendar.getTime()));

        return view;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String selectedDate = dateFormat.format(calendar.getTime());
                btnSelectDate.setText(selectedDate); // Update button text
                setupPieChart(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void setupPieChart(String date) {
        List<ToDoModel> tasks = myDB.getAllTasksForDate(date);

        Map<String, Integer> activityCount = new HashMap<>();
        for (ToDoModel task : tasks) {
            String activity = task.getActivity();
            activityCount.put(activity, activityCount.getOrDefault(activity, 0) + task.getDuration());
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        int personalTime = 0;
        int workSchoolTime = 0;

        for (Map.Entry<String, Integer> entry : activityCount.entrySet()) {
            String activity = entry.getKey();
            int minutes = entry.getValue();
            entries.add(new PieEntry(minutes, activity));

            if (activity.equalsIgnoreCase("Personal Time")) {
                personalTime += minutes;
            } else if (activity.equalsIgnoreCase("Work/School")) {
                workSchoolTime += minutes;
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Activities");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // refresh

        int sleepTime = calculateSleepTime(personalTime, workSchoolTime);
        displaySleepTime(sleepTime);
        evaluateSchedule(personalTime, workSchoolTime, sleepTime);
        displayActivityTimes(personalTime, workSchoolTime);
    }

    private int calculateSleepTime(int personalTime, int workSchoolTime) {
        int totalActivityTime = personalTime + workSchoolTime;
        if (totalActivityTime <= 16 * 60) {
            return 8 * 60; // 8 hours in minutes
        } else {
            int extraTime = totalActivityTime - 16 * 60;
            return (8 * 60) - extraTime;
        }
    }

    private void displaySleepTime(int sleepTime) {
        int hours = sleepTime / 60;
        int minutes = sleepTime % 60;
        String sleepTimeString = String.format("Sleep Time: %d hrs %d mins", hours, minutes);
        sleepTimeTextView.setText(sleepTimeString);
    }

    private void evaluateSchedule(int personalTime, int workSchoolTime, int sleepTime) {
        String evaluation;
        int sleepHours = sleepTime / 60;
        if (sleepHours >= 7 && sleepHours <= 9) {
            evaluation = "Your sleep schedule is good!";
        } else if (workSchoolTime > 480 || personalTime > 480) {
            evaluation = "Your schedule might be bad for your health.";
        } else {
            evaluation = "Consider adjusting your schedule for better sleep.";
        }
        evaluationTextView.setText(evaluation);
    }

    private void displayActivityTimes(int personalTime, int workSchoolTime) {
        int personalHours = personalTime / 60;
        int personalMinutes = personalTime % 60;
        String personalTimeString = String.format("Personal Time: %d hrs %d mins", personalHours, personalMinutes);
        personalTimeTextView.setText(personalTimeString);

        int workHours = workSchoolTime / 60;
        int workMinutes = workSchoolTime % 60;
        String workTimeString = String.format("Work/School Time: %d hrs %d mins", workHours, workMinutes);
        workTimeTextView.setText(workTimeString);
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }
}
