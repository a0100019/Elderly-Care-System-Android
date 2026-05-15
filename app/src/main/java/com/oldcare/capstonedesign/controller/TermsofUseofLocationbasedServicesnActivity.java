package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.oldcare.capstonedesign.databinding.ActivityTermsofUseofLocationbasedServicesnBinding;

import java.util.HashMap;
import java.util.Map;

public class TermsofUseofLocationbasedServicesnActivity extends AppCompatActivity {

    private ActivityTermsofUseofLocationbasedServicesnBinding binding;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsofUseofLocationbasedServicesnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        uid = preferences.getString("uid", "");

        binding.consentButton.setOnClickListener(v -> {
            showConfirmationDialog(true);
        });

        binding.rejectButton.setOnClickListener(v -> {
            showConfirmationDialog(false);
        });
    }

    private void showConfirmationDialog(final boolean consent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("동의 여부 확인");
        builder.setMessage("[어르신을 부탁해] 는 위치 데이터를 [노약자 경로확인], 어르신 주변 [복지기관 및 병원 조회], [어르신 안전구역 설정] 기능에 필요합니다. 앱을 사용하지 않을 때도 앱에서 내 위치에 항상 엑세스하려고 합니다. 동의하십니까?");
        builder.setPositiveButton("동의", (dialog, which) -> {
            if (consent) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("agree", 1);
                db.collection("Users")
                        .document(uid)
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "업데이트 성공");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "업데이트 실패", e);
                        });
                Intent intent = new Intent(this, LocationActivity.class);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        });
        builder.setNegativeButton("거부", (dialog, which) -> {
            onBackPressed();
        });
        builder.show();
    }
}
