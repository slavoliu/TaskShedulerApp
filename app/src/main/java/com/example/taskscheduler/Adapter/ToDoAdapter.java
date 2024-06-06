package com.example.taskscheduler.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskscheduler.AddNewTask;
import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.OnDialogCloseListener;
import com.example.taskscheduler.R;
import com.example.taskscheduler.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private final Fragment fragment;
    private final DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, Fragment fragment) {
        this.myDB = myDB;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                myDB.updateStatus(item.getId(), 1);
            } else {
                myDB.updateStatus(item.getId(), 0);
            }
        });

        // Set values for start time, activity, end time, and date
        holder.startTimeTextView.setText("Start: " + item.getStartTime());
        holder.activityTextView.setText(item.getActivity());
        holder.endTimeTextView.setText("End: " + item.getEndTime());
        holder.dateTextView.setText(item.getDate());

        holder.itemView.setOnClickListener(v -> {
            AddNewTask dialog = AddNewTask.newInstance(item);
            dialog.setOnDialogCloseListener((OnDialogCloseListener) fragment);
            dialog.show(fragment.getChildFragmentManager(), AddNewTask.TAG);
        });
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public void setTasks(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        AddNewTask dialog = AddNewTask.newInstance(item);
        dialog.setOnDialogCloseListener((OnDialogCloseListener) fragment);
        dialog.show(fragment.getChildFragmentManager(), AddNewTask.TAG);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView startTimeTextView;
        TextView activityTextView;
        TextView endTimeTextView;
        TextView dateTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            activityTextView = itemView.findViewById(R.id.activityTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
