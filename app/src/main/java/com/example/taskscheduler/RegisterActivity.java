package com.example.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.Manifest;


import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this,R.color.light_grey));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        db = new DatabaseHelper(this);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((TextInputEditText) findViewById(R.id.first_name)).getText().toString();
                String lastName = ((TextInputEditText) findViewById(R.id.last_name)).getText().toString();
                String email = ((TextInputEditText) findViewById(R.id.email)).getText().toString();
                String phone = ((TextInputEditText) findViewById(R.id.phone)).getText().toString();
                String country = ((TextInputEditText) findViewById(R.id.country)).getText().toString();
                String password = ((TextInputEditText) findViewById(R.id.password)).getText().toString();

                if (firstName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "First name is empty", Toast.LENGTH_SHORT).show();
                } else if (lastName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Last name is empty", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty() || !isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Email is empty or invalid", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty() || !isValidPhoneNumber(phone)) {
                    Toast.makeText(RegisterActivity.this, "Phone is empty or invalid", Toast.LENGTH_SHORT).show();
                } else if (country.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Country is empty", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(firstName, lastName, email, phone, country, password);
                    long result = db.addUser(user);

                    if (result > 0) {
                        Toast.makeText(RegisterActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);

                        startActivity(intent);

                        finish();


                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}