package com.example.taskscheduler;
import com.example.taskscheduler.CustomColorPallete;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private PieChart pieChart;
    private DataBaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        myDB = new DataBaseHelper(getContext());

        setupPieChart();

        return view;
    }

    public void setupPieChart() {
        List<ToDoModel> tasks = myDB.getAllTasks();

        Map<String, Integer> activityCount = new HashMap<>();
        for (ToDoModel task : tasks) {
            String activity = task.getActivity();
            activityCount.put(activity, activityCount.getOrDefault(activity, 0) + 1);
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
