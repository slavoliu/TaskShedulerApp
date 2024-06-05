package com.example.taskscheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView infoTextView = view.findViewById(R.id.infoTextView);

        String infoText = "The 8-8-8 model is a simple and effective way to structure a 24-hour day to balance work, rest, and personal time. " +
                "This model divides the day into three equal parts, each consisting of 8 hours:\n\n" +
                "1. 8 Hours of Work or School\n\n" +
                "This block is dedicated to your professional or educational commitments. It's important to use this time efficiently and effectively to complete your tasks.\n" +
                "- Focus and Productivity: Break your work into focused periods (e.g., Pomodoro Technique) to maximize productivity.\n" +
                "- Breaks: Take short breaks (5-10 minutes) every hour to rest and rejuvenate.\n" +
                "- Healthy Environment: Ensure your workspace is ergonomic and free from distractions.\n\n" +
                "2. 8 Hours of Sleep\n\n" +
                "Getting adequate sleep is crucial for physical and mental health. Aim for consistent sleep patterns.\n" +
                "- Sleep Hygiene: Maintain a regular sleep schedule, even on weekends.\n" +
                "- Environment: Create a comfortable and dark sleep environment.\n" +
                "- Wind Down: Develop a pre-sleep routine that helps you relax, such as reading or meditating.\n\n" +
                "3. 8 Hours of Personal Time\n\n" +
                "This segment is for personal activities that contribute to your well-being and happiness.\n" +
                "- Exercise: Incorporate at least 30 minutes of moderate physical activity.\n" +
                "- Meals: Prepare and enjoy balanced meals.\n" +
                "- Leisure: Engage in hobbies, spend time with family and friends, and relax.\n" +
                "- Self-Care: Practice mindfulness, meditation, or other activities that promote mental health.\n\n" +
                "Example Schedule Using the 8-8-8 Model\n\n" +
                "6:00 AM - 7:00 AM: Morning Routine\n" +
                "- Wake up, hydrate, and do light exercises.\n\n" +
                "7:00 AM - 8:00 AM: Breakfast\n" +
                "- Eat a healthy breakfast and prepare for the day.\n\n" +
                "8:00 AM - 12:00 PM: Work/School\n" +
                "- Focus on tasks with short breaks.\n\n" +
                "12:00 PM - 1:00 PM: Lunch Break\n" +
                "- Have a balanced meal and relax.\n\n" +
                "1:00 PM - 5:00 PM: Work/School\n" +
                "- Continue working or studying, with breaks.\n\n" +
                "5:00 PM - 6:00 PM: Physical Activity\n" +
                "- Engage in exercise or physical activities.\n\n" +
                "6:00 PM - 7:00 PM: Dinner\n" +
                "- Enjoy a healthy dinner.\n\n" +
                "7:00 PM - 9:00 PM: Leisure Time\n" +
                "- Spend time with family, pursue hobbies, or relax.\n\n" +
                "9:00 PM - 10:00 PM: Wind Down\n" +
                "- Prepare for bed by reducing screen time and relaxing.\n\n" +
                "10:00 PM - 6:00 AM: Sleep\n" +
                "- Ensure you get 8 hours of uninterrupted sleep.\n\n" +
                "Benefits of the 8-8-8 Model\n" +
                "- Balance: Ensures a balanced approach to life by allocating time for essential activities.\n" +
                "- Productivity: Helps maintain a high level of productivity during work hours.\n" +
                "- Health: Promotes better physical and mental health through adequate rest and personal time.\n" +
                "- Stress Reduction: Helps reduce stress by incorporating leisure and relaxation into daily routines.\n\n" +
                "Tips for Implementing the 8-8-8 Model\n" +
                "- Consistency: Try to stick to your schedule as closely as possible.\n" +
                "- Flexibility: Allow some flexibility to accommodate unexpected events.\n" +
                "- Prioritization: Prioritize tasks and activities to make the most of your time.\n\n" +
                "References\n" +
                "- CDC Guidelines on Physical Activity\n" +
                "- National Sleep Foundation on Sleep\n" +
                "- Harvard Health on Balanced Diet\n\n" +
                "By following the 8-8-8 model, you can create a well-rounded and healthy daily routine that supports both productivity and well-being.";

        infoTextView.setText(infoText);

        return view;
    }
}
