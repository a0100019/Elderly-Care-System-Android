package com.oldcare.capstonedesign.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.oldcare.capstonedesign.databinding.ActivityLoadingBinding;
import com.oldcare.capstonedesign.controller.OldMainActivity;
import com.oldcare.capstonedesign.controller.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

    private ActivityLoadingBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userInformation();
    }

    private void userInformation() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("Users").document(uid).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    if (document.contains("who")) {
                                        String who = Objects.requireNonNull(document.getString("who"));

                                        if (!who.equals("admin")) {

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("uid", uid);
                                            editor.putString("who", who);
                                            editor.apply();

                                            Intent intent1 = getIntent();
                                            String receivedVariable = intent1.getStringExtra("key");
                                            if(intent1 != null) {
                                                Intent intent = new Intent(LoadingActivity.this, OldServiceActivity.class);
                                                intent.putExtra("key", receivedVariable);
                                                startActivity(intent);
                                                finish();
                                            }
                                            Intent intent = new Intent(LoadingActivity.this, OldMainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            String userNickname = Objects.requireNonNull(document.getString("nickname"));
                                            String masterNumber = Objects.requireNonNull(document.getString("masterNumber"));
                                            String oldNumber = Objects.requireNonNull(document.getString("oldNumber"));

                                            String oldMan = document.getString("oldMan");
                                            if (oldMan == null) {
                                                oldMan = "";
                                            }

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("nickName", userNickname);
                                            editor.putString("who", who);
                                            editor.putString("masterNumber", masterNumber);
                                            editor.putString("oldNumber", oldNumber);
                                            editor.putString("oldMan", oldMan);
                                            editor.putString("uid", uid);
                                            editor.apply();

                                            Intent intent1 = getIntent();
                                            String receivedVariable = intent1.getStringExtra("key");
                                            if(intent1 != null) {
                                                Intent intent = new Intent(LoadingActivity.this, MasterServiceActivity.class);
                                                intent.putExtra("key", receivedVariable);
                                                startActivity(intent);
                                                finish();
                                            }
                                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        Intent intent = new Intent(LoadingActivity.this, StartActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        }
                    });
        }
    }
}
