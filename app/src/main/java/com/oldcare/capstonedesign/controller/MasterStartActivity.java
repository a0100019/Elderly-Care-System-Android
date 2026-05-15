package com.oldcare.capstonedesign.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.oldcare.capstonedesign.controller.MasterLoadingActivity;
import com.oldcare.capstonedesign.databinding.ActivityMasterStartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MasterStartActivity extends AppCompatActivity {

    private ActivityMasterStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        binding.masterStartYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();

                    DocumentReference userRef = db.collection("Users").document(uid);
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {

                                    String masterNumber = Objects.requireNonNull(binding.masterStartText.getText()).toString();
                                    String masterNumber2 = Objects.requireNonNull(binding.passwordText.getText()).toString();

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("who", "admin");
                                    updates.put("masterNumber", masterNumber);
                                    updates.put("oldNumber", masterNumber2);
                                    updates.put("stepGoal", "10000 걸음 도전해봅시다ㅎㅎ");

                                    userRef.update(updates)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MasterStartActivity.this, "보호자로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MasterStartActivity.this, MasterLoadingActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MasterStartActivity.this, "업데이트를 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
