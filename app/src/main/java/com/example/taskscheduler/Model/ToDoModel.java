package com.example.taskscheduler.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToDoModel {
    private int id;
    private String task;
    private String date;
    private String startTime;
    private String endTime;
    private String activity;
    private int status;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Helper method to get task duration in minutes
    public int getDuration() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            if (start != null && end != null) {
                return (int) ((end.getTime() - start.getTime()) / (1000 * 60));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
