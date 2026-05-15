package com.oldcare.capstonedesign.controller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.oldcare.capstonedesign.databinding.ActivityMasterLoadingBinding;
import com.oldcare.capstonedesign.view.MasterAlarmFragment;
import com.oldcare.capstonedesign.view.MasterCommunityFragment;
import com.oldcare.capstonedesign.view.MasterLoadingFragment;
import com.oldcare.capstonedesign.view.MasterSettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MasterLoadingActivity extends AppCompatActivity {

    private ActivityMasterLoadingBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SettingListener();
        binding.bottomNavi.setSelectedItemId(R.id.item_loading_fragment);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        db.collection("Users").document(uid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d(TAG, "Current data: " + documentSnapshot.getData());

                            if (documentSnapshot.contains("oldMan")) {
                                Log.d(TAG, "oldMan 필드가 추가됨");
                                Intent intent = new Intent(MasterLoadingActivity.this, LoadingActivity.class);
                                intent.putExtra("key", "first");
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.item_loading_fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MasterLoadingFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.item_main_fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MasterAlarmFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.item_community_fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MasterCommunityFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.item_my_fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MasterSettingFragment())
                        .commit();
                return true;
            }

            return false;
        }
    }

    private void SettingListener() {
        binding.bottomNavi.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }
}
