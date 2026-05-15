package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.oldcare.capstonedesign.databinding.ActivityWarningBinding;

public class WarningActivity extends AppCompatActivity {

    private ActivityWarningBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView19.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("https://sable-walrus-280.notion.site/8b9bb038a2ef48558f728c83a589d35e?pvs=4");
            intent.setData(uri);
            startActivity(intent);
        });
    }
}
