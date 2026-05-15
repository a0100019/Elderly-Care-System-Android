package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.oldcare.capstonedesign.databinding.ActivityOldWarningBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OldWarningActivity extends AppCompatActivity {

    private ActivityOldWarningBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldWarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("121212", "클릭 성공");

                db.collection("Users")
                        .document(uid)
                        .collection("message")
                        .limit(1)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot latestDocument = queryDocumentSnapshots.getDocuments().get(0);
                                Log.d("121212", "문서 가져옴");

                                String documentId = latestDocument.getId();
                                db.collection("Users")
                                        .document(uid)
                                        .collection("message")
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
                                            String who = preferences.getString("who", "");

                                            Map<String, Object> newDocument = new HashMap<>();
                                            newDocument.put("title", "확인");

                                            db.collection("Users").document(who)
                                                    .collection("message")
                                                    .add(newDocument)
                                                    .addOnSuccessListener(documentReference -> {
                                                        Log.d("Firestore", "New document added with ID: " + documentReference.getId());
                                                        Intent intent = new Intent(OldWarningActivity.this, OldMainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                        Log.d("121212", "삭제 성공");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("Firestore", "Error adding document: " + e.getMessage());
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.d("121212", "삭제 실패");
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.d("121212", "쿼리 실패");
                        });
            }
        });
    }
}
