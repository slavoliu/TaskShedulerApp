package com.example.taskscheduler;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskscheduler.Adapter.ToDoAdapter;
import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private DataBaseHelper myDB;
    private FloatingActionButton fabAddTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myDB = new DataBaseHelper(getContext());
        toDoAdapter = new ToDoAdapter(myDB, this);
        recyclerView.setAdapter(toDoAdapter);

        fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            AddNewTask dialog = AddNewTask.newInstance();
            dialog.setOnDialogCloseListener(this);
            dialog.show(getChildFragmentManager(), AddNewTask.TAG);
        });

        // Add ItemTouchHelper to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(toDoAdapter, requireContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        refreshData();
        return view;
    }

    public void refreshData() {
        List<ToDoModel> updatedList = myDB.getAllTasks();
        toDoAdapter.setTasks(updatedList);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        refreshData();
    }
}
