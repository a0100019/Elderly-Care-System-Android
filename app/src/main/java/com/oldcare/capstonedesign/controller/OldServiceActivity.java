package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.oldcare.capstonedesign.databinding.ActivityOldServiceBinding;
import com.oldcare.capstonedesign.controller.OldMainActivity;
import com.oldcare.capstonedesign.service.FirestoreNotificationService;
import com.oldcare.capstonedesign.service.StepCounterService;

public class OldServiceActivity extends AppCompatActivity {

    private ActivityOldServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent1 = getIntent();
        if(intent1 != null) {
            String receivedVariable = intent1.getStringExtra("key");
            if(receivedVariable != null && receivedVariable.equals("first2223")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent = new Intent(OldServiceActivity.this, FirestoreNotificationService.class);
                    startService(serviceIntent);
                    Intent serviceIntent1 = new Intent(OldServiceActivity.this, StepCounterService.class);
                    startService(serviceIntent1);
                }
                Toast.makeText(OldServiceActivity.this, "보호자와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OldServiceActivity.this, OldMainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        binding.alarmOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent = new Intent(OldServiceActivity.this, FirestoreNotificationService.class);
                    startService(serviceIntent);
                }
            }
        });

        binding.alarmOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(OldServiceActivity.this, FirestoreNotificationService.class);
                stopService(serviceIntent);
            }
        });

        binding.stepOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent1 = new Intent(OldServiceActivity.this, StepCounterService.class);
                    startService(serviceIntent1);
                }
            }
        });

        binding.stepOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent1 = new Intent(OldServiceActivity.this, StepCounterService.class);
                stopService(serviceIntent1);
            }
        });

        binding.alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        });
    }
}
