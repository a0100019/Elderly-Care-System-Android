package com.oldcare.capstonedesign.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oldcare.capstonedesign.databinding.ActivityStepCounterBinding;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class StepCounterActivity extends AppCompatActivity {

    private ActivityStepCounterBinding binding;
    ArrayList<Integer> jsonList = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();
    int lastValue;
    private String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepCounterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        graphInitSetting();

        BarChartGraph(labelList, jsonList);
        binding.fragmentBluetoothChatBarchart.setTouchEnabled(false);

        binding.cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStepGoalDialog();
            }
        });
    }

    private void showStepGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어르신의 핸드폰에 표시될 일일 걸음 목표를 작성해주세요.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String stepGoal = input.getText().toString().trim();
                updateStepGoal(stepGoal);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateStepGoal(String stepGoal) {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        DocumentReference userRef = db.collection("Users").document(uid);

        userRef
                .update("stepGoal", stepGoal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(StepCounterActivity.this, "설정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StepCounterActivity.this, "설정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void graphInitSetting(){

        Log.e("aaaaa", "문서 가져옴2");

        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String oldMan = preferences.getString("oldMan", "");
        db.collection("Users")
                .document(oldMan)
                .collection("steps")
                .limit(7)
                .get()
                .addOnCompleteListener(task -> {
                    Log.e("aaaaa", "문서 가져옴1");

                    if (task.isSuccessful()) {
                        Log.e("aaaaa", "문서 가져옴4");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentName = document.getId();
                            String label = documentName.substring(Math.max(0, documentName.length() - 2)) + "일";
                            labelList.add(label);

                            Log.e("aaaaa", "문서 가져옴");

                            Long step = document.getLong("step");
                            double stepValue = (step != null) ? step.intValue() : 0;
                            jsonList.add((int) stepValue);
                            binding.tvSteps.setText(String.format("%.0f", stepValue));
                        }

                        Log.e("aaaaa", "문서 가져옴5");

                        double sum = 0;
                        for (Number value : jsonList) {
                            sum += value.doubleValue();
                        }
                        double average = sum / jsonList.size();

                        binding.totalStepTextView.setText(String.format("%.0f", average) + " 걸음");

                        if (!jsonList.isEmpty()) {
                            lastValue = jsonList.get(jsonList.size() - 1).intValue();
                            String goalResult = (lastValue >= 10000) ? "달성" : "미달성";
                            binding.goalTextView.setText(goalResult);
                        } else {
                            binding.goalTextView.setText("데이터 없음");
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
