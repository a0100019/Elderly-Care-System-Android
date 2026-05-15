package com.oldcare.capstonedesign.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.oldcare.capstonedesign.databinding.ActivityOldStepCounterBinding;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OldStepCounterActivity extends AppCompatActivity {

    private ActivityOldStepCounterBinding binding;
    private StepReceiver stepReceiver;
    ArrayList<Integer> jsonList = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();
    int lastValue;
    private String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldStepCounterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        stepReceiver = new StepReceiver();

        graphInitSetting();

        binding.fragmentBluetoothChatBarchart.setTouchEnabled(false);

        masterGoal();
    }

    private void masterGoal() {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String who = preferences.getString("who", "");
        db.collection("Users")
                .document(who)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String stepGoal = documentSnapshot.getString("stepGoal");
                        if (stepGoal != null) {
                            binding.masterGoalTextView.setText(stepGoal);
                            Log.d("Firestore", "stepGoal: " + stepGoal);
                        } else {
                            Log.d("Firestore", "stepGoal is not available");
                        }
                    } else {
                        Log.d("Firestore", "Document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting document: " + e.getMessage());
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(stepReceiver, new IntentFilter("com.oldcare.stepcountapp.STEP_UPDATE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(stepReceiver);
    }

    private class StepReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double steps = intent.getIntExtra("steps", 0);
            binding.tvSteps.setText(String.format("%.0f", steps));

            SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
            String uid = preferences.getString("uid", "");

            DocumentReference userStepsRef = db.collection("Users").document(uid).collection("steps").document(currentDate);

                userStepsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            userStepsRef.update("step", steps)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("Document updated successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println("Error updating document: " + e);
                                        }
                                    });
                        } else {
                            Map<String, Object> data = new HashMap<>();
                            data.put("step", steps);

                            userStepsRef.set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("Document saved successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println("Error saving document: " + e);
                                        }
                                    });
                        }
                    }
                });
            }
        }

    private void stopStepCounterService() {
    }

    public void graphInitSetting(){
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");

        db.collection("Users")
                .document(uid)
                .collection("steps")
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        int documentCount = task1.getResult().size();
                        if (documentCount >= 8) {
                            List<DocumentSnapshot> documents = task1.getResult().getDocuments();
                            DocumentSnapshot firstDocument = documents.get(0);
                            firstDocument.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "First document deleted successfully");
                                        db.collection("Users")
                                                .document(uid)
                                                .collection("steps")
                                                .limit(7)
                                                .get()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String documentName = document.getId();
                                                            String label = documentName.substring(Math.max(0, documentName.length() - 2)) + "일";
                                                            labelList.add(label);

                                                            Long step = document.getLong("step");
                                                            double stepValue = (step != null) ? step.intValue() : 0;
                                                            jsonList.add((int) stepValue);
                                                            binding.tvSteps.setText(String.format("%.0f", stepValue));
                                                        }

                                                        double sum = 0;
                                                        for (Number value : jsonList) {
                                                            sum += value.doubleValue();
                                                        }
                                                        double average = sum / jsonList.size();

                                                        if (!Double.isNaN(average)) {
                                                            binding.totalStepTextView.setText(String.format("%.0f", average) + " 걸음");
                                                        } else {
                                                            binding.totalStepTextView.setText("0 걸음");
                                                        }

                                                        if (!jsonList.isEmpty()) {
                                                            lastValue = jsonList.get(jsonList.size() - 1).intValue();
                                                            String goalResult = (lastValue >= 10000) ? "달성" : "미달성";
                                                            binding.goalTextView.setText(goalResult);
                                                        } else {
                                                            binding.goalTextView.setText("미달성");
                                                        }

                                                        int remaining = 7 - labelList.size();
                                                        for (int i = 0; i < remaining; i++) {
                                                            labelList.add("-");
                                                            jsonList.add(0);
                                                        }

                                                        BarChartGraph(labelList, jsonList);
                                                        binding.fragmentBluetoothChatBarchart.setTouchEnabled(false);
                                                        binding.fragmentBluetoothChatBarchart.setAutoScaleMinMaxEnabled(true);
                                                    } else {
                                                        Exception exception = task.getException();
                                                        if (exception != null) {
                                                            Log.e("Firestore", "Error getting documents: " + exception.getMessage());
                                                        }
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error deleting first document: " + e.getMessage());
                                    });
                        } else {
                            db.collection("Users")
                                    .document(uid)
                                    .collection("steps")
                                    .limit(7)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String documentName = document.getId();
                                                String label = documentName.substring(Math.max(0, documentName.length() - 2)) + "일";
                                                labelList.add(label);

                                                Long step = document.getLong("step");
                                                double stepValue = (step != null) ? step.intValue() : 0;
                                                jsonList.add((int) stepValue);
                                                binding.tvSteps.setText(String.format("%.0f", stepValue));
                                            }

                                            double sum = 0;
                                            for (Number value : jsonList) {
                                                sum += value.doubleValue();
                                            }
                                            double average = sum / jsonList.size();

                                            if (!Double.isNaN(average)) {
                                                binding.totalStepTextView.setText(String.format("%.0f", average) + " 걸음");
                                            } else {
                                                binding.totalStepTextView.setText("0 걸음");
                                            }

                                            if (!jsonList.isEmpty()) {
                                                lastValue = jsonList.get(jsonList.size() - 1).intValue();
                                                String goalResult = (lastValue >= 10000) ? "달성" : "미달성";
                                                binding.goalTextView.setText(goalResult);
                                            } else {
                                                binding.goalTextView.setText("미달성");
                                            }

                                            int remaining = 7 - labelList.size();
                                            for (int i = 0; i < remaining; i++) {
                                                labelList.add("-");
                                                jsonList.add(0);
                                            }

                                            BarChartGraph(labelList, jsonList);
                                            binding.fragmentBluetoothChatBarchart.setTouchEnabled(false);
                                            binding.fragmentBluetoothChatBarchart.setAutoScaleMinMaxEnabled(true);
                                        } else {
                                            Exception exception = task.getException();
                                            if (exception != null) {
                                                Log.e("Firestore", "Error getting documents: " + exception.getMessage());
                                            }
                                        }
                                    });
                        }
                    } else {
                        Exception exception = task1.getException();
                        if (exception != null) {
                            Log.e("Firestore", "Error getting documents: " + exception.getMessage());
                        }
                    }
                });
    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry((Integer) valList.get(i), i));
        }

        BarDataSet depenses = new BarDataSet(entries, "일일 걸음 수");
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);
        binding.fragmentBluetoothChatBarchart.setDescription(" ");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(labels, depenses);
        depenses.setColors(ColorTemplate.LIBERTY_COLORS);

        binding.fragmentBluetoothChatBarchart.setData(data);
        binding.fragmentBluetoothChatBarchart.animateXY(1000, 1000);
        binding.fragmentBluetoothChatBarchart.invalidate();

        int count = lastValue;
        db.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    AtomicInteger greaterCountDocuments = new AtomicInteger();
                    greaterCountDocuments.getAndIncrement();

                    for (QueryDocumentSnapshot userDocument : queryDocumentSnapshots) {
                        db.collection("Users")
                                .document(userDocument.getId())
                                .collection("steps")
                                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                                .limit(1)
                                .get()
                                .addOnSuccessListener(stepsSnapshots -> {
                                    if (!stepsSnapshots.isEmpty()) {
                                        QueryDocumentSnapshot stepDocument = (QueryDocumentSnapshot) stepsSnapshots.getDocuments().get(0);
                                        Long stepValue = stepDocument.getLong("step");

                                        if (stepValue != null && stepValue > count) {
                                            greaterCountDocuments.getAndIncrement();
                                            Log.d("Firestore", "User " + userDocument.getId() + " has a step count greater than " + count);
                                        }
                                        binding.rankTextView.setText(greaterCountDocuments.toString() + "등");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error getting steps document: " + e.getMessage());
                                });
                    }

                    Log.d("Firestore", "Total documents with steps greater than " + count + ": " + greaterCountDocuments);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting user documents: " + e.getMessage());
                });
    }
}
