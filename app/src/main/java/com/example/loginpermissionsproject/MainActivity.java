package com.example.loginpermissionsproject;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView main_LBL_message;

    private MaterialTextView main_LBL_header;
    private MaterialButton main_BTN_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }


    private void initViews() {
        main_BTN_exit.setOnClickListener(v -> exitApp());
    }

    private void findViews() {
        main_LBL_message = findViewById(R.id.main_LBL_message);
        main_BTN_exit = findViewById(R.id.main_BTN_exit);
    }

    private void exitApp() {
        finish(); // Close the current activity
        moveTaskToBack(true); // Move the task containing this activity to the back of the activity stack
        android.os.Process.killProcess(android.os.Process.myPid()); // Kill the current process, effectively closing the app
        System.exit(1); // Exit the application
    }

}