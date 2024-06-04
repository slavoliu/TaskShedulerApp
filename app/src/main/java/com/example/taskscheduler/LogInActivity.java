package com.example.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this,R.color.light_grey));
        }

        TextView textView = findViewById(R.id.registerPrompt);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

                startActivity(intent);
            }
        });


        Button signInButton = findViewById(R.id.btnSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = ((EditText) findViewById(R.id.edtTxtEmail)).getText().toString();
                String passwordInput = ((EditText) findViewById(R.id.edtTxtPassword)).getText().toString();

                DatabaseHelper db = new DatabaseHelper(LogInActivity.this);
                boolean userExists = db.checkUser(emailInput, passwordInput);

                if (userExists) {
                    Intent intent = new Intent(LogInActivity.this, UserActivity.class);
                    intent.putExtra("email",emailInput);
                    startActivity(intent);
                    Toast.makeText(LogInActivity.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}