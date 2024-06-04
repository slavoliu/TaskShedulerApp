package com.example.taskscheduler;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskscheduler.Adapter.ToDoAdapter;
import com.example.taskscheduler.Model.ToDoModel;
import com.example.taskscheduler.Utils.DataBaseHelper;
import com.example.taskscheduler.databinding.ActivityUser2Binding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserActivity extends AppCompatActivity implements OnDialogCloseListener {

    @NonNull
    ActivityUser2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUser2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // Map to associate menu item IDs with fragments
            Map<Integer, Fragment> fragmentMap = new HashMap<>();
            fragmentMap.put(R.id.profile, new ProfileFragment());
            fragmentMap.put(R.id.info, new InfoFragment());

            // Get the selected fragment based on the menu item ID
            Fragment selectedFragment = fragmentMap.get(item.getItemId());
            // If the selected fragment is null, default to HomeFragment
            if (selectedFragment == null) {
                selectedFragment = new HomeFragment();
            }

            // Replace the current fragment with the selected fragment
            replaceFragment(selectedFragment);
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).refreshData();
        }
    }
}
