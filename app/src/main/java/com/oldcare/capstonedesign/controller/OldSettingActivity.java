package com.oldcare.capstonedesign.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.oldcare.capstonedesign.controller.OldServiceActivity;
import com.oldcare.capstonedesign.controller.WarningActivity;
import com.oldcare.capstonedesign.databinding.ActivityOldSettingBinding;
import com.oldcare.capstonedesign.controller.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OldSettingActivity extends AppCompatActivity {

    private ActivityOldSettingBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signOut();
        deleteID();
        notification();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String who = preferences.getString("who", "");

        db.collection("Users").document(who).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nickname = documentSnapshot.getString("nickname");
                        String masterNumber = documentSnapshot.getString("masterNumber");
                        String oldNumber = documentSnapshot.getString("oldNumber");

                        if (nickname != null) {
                            binding.informationTextView.setText(nickname);
                        }
                        if (masterNumber != null) {
                            binding.masterNumberTextView.setText(masterNumber);
                        }
                        if (oldNumber != null) {
                            binding.oldNumberTextView.setText(oldNumber);
                        }
                    }
                });

        binding.oldWarningChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldSettingActivity.this, WarningActivity.class);
                startActivity(intent);
            }
        });
    }

    private void notification() {
        binding.finishChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldSettingActivity.this, OldServiceActivity.class);
                intent.putExtra("key", "second");
                startActivity(intent);
                finish();
            }
        });
    }

    private void signOut() {
        binding.oldSignOutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(OldSettingActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void deleteID() {
        mAuth = FirebaseAuth.getInstance();

        binding.oldDeleteIDChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("계정을 삭제하시겠습니까? 삭제 후에는 복구할 수 없습니다.")
                .setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            currentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mAuth.signOut();
                                                Toast.makeText(OldSettingActivity.this, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(OldSettingActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                if (task.getException().getMessage().contains("requires recent authentication")) {
                                                    Toast.makeText(OldSettingActivity.this, "로그인이 오래되었습니다. 재 로그인 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(OldSettingActivity.this, "계정 삭제 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
