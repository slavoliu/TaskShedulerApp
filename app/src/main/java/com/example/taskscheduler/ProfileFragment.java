package com.example.taskscheduler;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private PieChart pieChart;
    private DataBaseHelper myDB;
    private Button btnSelectDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
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

    public void setupPieChart(String date) {
        List<ToDoModel> tasks = myDB.getAllTasks();

        Map<String, Integer> activityCount = new HashMap<>();
        for (ToDoModel task : tasks) {
            if (task.getDate().equals(date)) {
                String activity = task.getActivity();
                activityCount.put(activity, activityCount.getOrDefault(activity, 0) + 1);
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : activityCount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Activities");
        dataSet.setColors(CustomColorPallete.getColors()); // Use custom color palette

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
