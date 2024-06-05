package com.example.taskscheduler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.OnDialogCloseListener;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewTask extends DialogFragment {

    public static final String TAG = "add_new_task";

    private TextInputEditText taskEditText, dateEditText, startTimeEditText, endTimeEditText;
    private Spinner activitySpinner;
    private Button saveButton, cancelButton;

    private DataBaseHelper myDB;
    private OnDialogCloseListener dialogCloseListener;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    public void setOnDialogCloseListener(OnDialogCloseListener listener) {
        this.dialogCloseListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_newtask, container, false);

        taskEditText = view.findViewById(R.id.editTextTask);
        dateEditText = view.findViewById(R.id.editTextDate);
        startTimeEditText = view.findViewById(R.id.editTextStartTime);
        endTimeEditText = view.findViewById(R.id.editTextEndTime);
        activitySpinner = view.findViewById(R.id.spinnerActivity);
        saveButton = view.findViewById(R.id.button_save);
        cancelButton = view.findViewById(R.id.button_cancel);

        myDB = new DataBaseHelper(getActivity());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Sample items for the dropdown menu
        String[] activities = new String[]{"Personal Time", "Work/School"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        activitySpinner.setAdapter(adapter);

        // Listen for changes in date EditText
        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatDateTimeInput(s, dateEditText, "dd/MM/yyyy");
            }
        });

        // Listen for changes in start time EditText
        startTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatDateTimeInput(s, startTimeEditText, "HH:mm");
            }
        });

        // Listen for changes in end time EditText
        endTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                formatDateTimeInput(s, endTimeEditText, "HH:mm");
            }
        });

        return view;
    }

    private void saveTask() {
        String task = taskEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String startTime = startTimeEditText.getText().toString();
        String endTime = endTimeEditText.getText().toString();
        String activity = activitySpinner.getSelectedItem().toString();

        // Check if any field is empty
        if (task.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || activity.isEmpty()) {
            // Show a toast or alert the user to fill in all fields
            return;
        }

        // Save task to database
        ToDoModel item = new ToDoModel();
        item.setTask(task);
        item.setDate(date);
        item.setStartTime(startTime);
        item.setEndTime(endTime);
        item.setActivity(activity);
        item.setStatus(0); // Assuming the default status is 0
        myDB.insertTask(item);

        // Refresh the data in the ProfileFragment
        ProfileFragment profileFragment = (ProfileFragment) getParentFragmentManager().findFragmentById(R.id.pieChart);
        if (profileFragment != null) {
            profileFragment.setupPieChart(date);
        }

        // Refresh the data in the fragment
        if (dialogCloseListener != null) {
            dialogCloseListener.onDialogClose(getDialog());
        }

        dismiss();
    }



    private void formatDateTimeInput(Editable s, TextInputEditText editText, String format) {
        String input = s.toString();
        String formattedInput = "";

        if (format.equals("dd/MM/yyyy")) {
            // Format for date input
            if (input.length() == 2 || input.length() == 5) {
                if (!input.endsWith("/")) {
                    formattedInput = input + "/";
                    editText.setText(formattedInput);
                    editText.setSelection(formattedInput.length());
                }
            } else if (input.length() > 2 && !input.contains("/")) {
                formattedInput = input.substring(0, 2) + "/" + input.substring(2);
                editText.setText(formattedInput);
                editText.setSelection(formattedInput.length());
            }
        } else if (format.equals("HH:mm")) {
            // Format for time input
            if (input.length() == 2) {
                if (!input.endsWith(":")) {
                    formattedInput = input + ":";
                    editText.setText(formattedInput);
                    editText.setSelection(formattedInput.length());
                }
            } else if (input.length() > 2 && !input.contains(":")) {
                formattedInput = input.substring(0, 2) + ":" + input.substring(2);
                editText.setText(formattedInput);
                editText.setSelection(formattedInput.length());
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
