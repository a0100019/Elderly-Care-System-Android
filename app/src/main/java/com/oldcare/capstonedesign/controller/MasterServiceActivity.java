package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.oldcare.capstonedesign.databinding.ActivityMasterServiceBinding;
import com.oldcare.capstonedesign.service.FirestoreNotificationService2;

public class MasterServiceActivity extends AppCompatActivity {

    private ActivityMasterServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent1 = getIntent();
        if(intent1 != null) {
            String receivedVariable = intent1.getStringExtra("key");
            if(receivedVariable.equals("first")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent = new Intent(MasterServiceActivity.this, FirestoreNotificationService2.class);
                    startService(serviceIntent);
                }
                Toast.makeText(MasterServiceActivity.this, "어르신과 연결되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MasterServiceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        binding.alarmOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent serviceIntent = new Intent(MasterServiceActivity.this, FirestoreNotificationService2.class);
                    startService(serviceIntent);
                }
            }
        });

        binding.alarmOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MasterServiceActivity.this, FirestoreNotificationService2.class);
                stopService(serviceIntent);
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
