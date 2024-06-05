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
        List<ToDoModel> tasks = myDB.getAllTasks();

        float workTime = 0;
        float personalTime = 0;
        float sleepTime = 8; // Default sleep time

        for (ToDoModel task : tasks) {
            if (task.getDate().equals(date)) {
                float startTime = Float.parseFloat(task.getStartTime().replace(":", "."));
                float endTime = Float.parseFloat(task.getEndTime().replace(":", "."));
                float duration = endTime - startTime;

                if (task.getActivity().equalsIgnoreCase("work") || task.getActivity().equalsIgnoreCase("school")) {
                    workTime += duration;
                } else {
                    personalTime += duration;
                }
            }
        }

        if (workTime + personalTime > 16) {
            sleepTime = 24 - (workTime + personalTime);
        }

        // Update TextViews
        sleepTimeTextView.setText(String.format(Locale.getDefault(), "Sleep Time: %.1f hrs", sleepTime));
        workTimeTextView.setText(String.format(Locale.getDefault(), "Work/School Time: %.1f hrs", workTime));
        personalTimeTextView.setText(String.format(Locale.getDefault(), "Personal Time: %.1f hrs", personalTime));
        if (workTime > 8 || personalTime > 8) {
            evaluationTextView.setText("Evaluation: Bad for your program");
        } else {
            evaluationTextView.setText("Evaluation: Good for your program");
        }

        // Load PieChart data
        loadPieChartData(workTime, personalTime, sleepTime);
    }

    private void loadPieChartData(float workTime, float personalTime, float sleepTime) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(workTime, "Work/School"));
        entries.add(new PieEntry(personalTime, "Personal"));
        entries.add(new PieEntry(sleepTime, "Sleep"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Activities");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // refresh
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
