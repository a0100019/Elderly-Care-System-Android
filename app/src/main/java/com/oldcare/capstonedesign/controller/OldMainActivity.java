package com.oldcare.capstonedesign.controller;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oldcare.capstonedesign.R;
import com.oldcare.capstonedesign.databinding.ActivityOldMainBinding;
import com.oldcare.capstonedesign.controller.TermsofUseofLocationbasedServicesnActivity_oldman;
import com.oldcare.capstonedesign.controller.bglocationactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OldMainActivity extends AppCompatActivity {

    private ActivityOldMainBinding binding;
    private Handler handler = new Handler();
    private Runnable periodicTask;
    private TextToSpeech tts;
    private FirebaseFirestore db;
    private String uid;
    private String ttsString1 = "";
    private String ttsString2 = "";
    private String ttsString3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOldMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SettingListener();
        binding.bottomNavigation.setSelectedItemId(R.id.item_main_fragment);

        db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        uid = preferences.getString("uid", "");
        CollectionReference collectionReference = db.collection("Users").document(uid).collection("message");

        Query query = collectionReference
                .orderBy("check", Query.Direction.ASCENDING)
                .orderBy("day", Query.Direction.ASCENDING)
                .orderBy("hour", Query.Direction.ASCENDING)
                .orderBy("minute", Query.Direction.ASCENDING)
                .limit(3);

        periodicTask = new Runnable() {
            @Override
            public void run() {
                memoLoad(query);
                handler.postDelayed(this, 2000);
            }
        };

        handler.post(periodicTask);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                    tts.setSpeechRate(0.8f);
                }
            }
        });

        binding.yesButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    if (!documents.isEmpty()) {
                        DocumentSnapshot firstDocument = documents.get(0);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.getDefault());
                        String currentTime = dateFormat.format(new Date());

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("time", currentTime);
                        updates.put("check", "1");

                        firstDocument.getReference().update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(OldMainActivity.this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "업데이트 실패", e);
                                });
                    } else {
                        Log.d("Firestore", "문서가 없습니다.");
                    }
                });
            }
        });

        binding.ttsButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(ttsString1, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        binding.yesButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    if (!documents.isEmpty()) {
                        DocumentSnapshot firstDocument = documents.get(1);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.getDefault());
                        String currentTime = dateFormat.format(new Date());

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("time", currentTime);
                        updates.put("check", "1");

                        firstDocument.getReference().update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(OldMainActivity.this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "업데이트 실패", e);
                                });
                    } else {
                        Log.d("Firestore", "문서가 없습니다.");
                    }
                });
            }
        });

        binding.ttsButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(ttsString2, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        binding.yesButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    if (!documents.isEmpty()) {
                        DocumentSnapshot firstDocument = documents.get(2);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.getDefault());
                        String currentTime = dateFormat.format(new Date());

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("time", currentTime);
                        updates.put("check", "1");

                        firstDocument.getReference().update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(OldMainActivity.this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "업데이트 실패", e);
                                });
                    } else {
                        Log.d("Firestore", "문서가 없습니다.");
                    }
                });
            }
        });

        binding.ttsButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(ttsString3, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.navigation_item1) {
                Intent intent = new Intent(getApplicationContext(), OldStepCounterActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_item2) {
                db.collection("Users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                int agreeValue = documentSnapshot.getLong("agree") != null ?
                                        documentSnapshot.getLong("agree").intValue() : 0;

                                if (agreeValue == 1) {
                                    Intent intent = new Intent(OldMainActivity.this, bglocationactivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(OldMainActivity.this, TermsofUseofLocationbasedServicesnActivity_oldman.class);
                                    startActivity(intent);
                                }
                            }
                        });
                return true;
            } else if (itemId == R.id.navigation_item3) {
                Intent intent = new Intent(getApplicationContext(), OldSettingActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        }
    }

    private void SettingListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    private void memoLoad(Query query) {
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                        if (documents.isEmpty()) {
                            Log.d("Firestore", "문서가 없습니다.");
                            return;
                        }

                        List<DocumentSnapshot> resultDocuments;
                        if (documents.size() >= 3) {
                            resultDocuments = documents.subList(0, 3);
                        } else {
                            resultDocuments = documents;
                        }

                        for (int i = 1; i <= resultDocuments.size(); i++) {
                            String check = resultDocuments.get(i - 1).getString("check");
                            if ( check.equals("0")) {
                                Long day = resultDocuments.get(i - 1).getLong("day");
                                String dayString = (day != null) ? String.valueOf(day) : "";

                                Long hour = resultDocuments.get(i - 1).getLong("hour");
                                String hourString = (hour != null) ? String.valueOf(hour) : "";

                                Long minute = resultDocuments.get(i - 1).getLong("minute");
                                String minuteString = (minute != null) ? String.valueOf(minute) : "";

                                String title = resultDocuments.get(i - 1).getString("title");
                                String titleString = (title != null) ? title : "";

                                String content = resultDocuments.get(i - 1).getString("content");
                                String contentString = (content != null) ? content : "";

                                String daytime = "";
                                if (dayString.equals("0")) {
                                    daytime = "오늘";
                                } else if (dayString.equals("1")) {
                                    daytime = "내일";
                                }  else if (dayString.equals("2")) {
                                    daytime = "모래";
                                } else if (dayString.equals("3")) {
                                    daytime = "사흘 뒤";
                                } else if (dayString.equals("4")) {
                                    daytime = "나흘 뒤";
                                }

                                String time = daytime + " " + hourString + "시 " + minuteString + "분";

                                if (i == 1) {
                                    ttsString1 = "보호자께서 보내신 메모를 읽어드리겠습니다. " + contentString + "이며. 시간은 " + time + " 입니다.";
                                } else if (i == 2) {
                                    ttsString2 = "보호자께서 보내신 메모를 읽어드리겠습니다. " + contentString + "이며. 시간은 " + time + " 입니다.";
                                } else {
                                    ttsString3 = "보호자께서 보내신 메모를 읽어드리겠습니다. " + contentString + "이며. 시간은 " + time + " 입니다.";
                                }

                                int titleTextViewId = getResources().getIdentifier("titleTextView" + i, "id", getPackageName());
                                TextView titleTextView = findViewById(titleTextViewId);
                                if (titleTextView != null) {
                                    titleTextView.setText(titleString);
                                } else {
                                    titleTextView.setText("");
                                }
                                int timeTextViewId = getResources().getIdentifier("timeTextView" + i, "id", getPackageName());
                                TextView timeTextView = findViewById(timeTextViewId);
                                if (timeTextView != null) {
                                    timeTextView.setText(time);
                                }else {
                                    titleTextView.setText("");
                                }
                                int contentTextViewId = getResources().getIdentifier("contentTextView" + i, "id", getPackageName());
                                TextView contentTextView = findViewById(contentTextViewId);
                                if (contentTextView != null) {
                                    contentTextView.setText(contentString);
                                }else {
                                    titleTextView.setText("");
                                }
                                int yesButtonId = getResources().getIdentifier("yesButton" + i, "id", getPackageName());
                                Button yesButton = findViewById(yesButtonId);
                                if (hourString.equals("")) {
                                    yesButton.setVisibility(View.INVISIBLE);
                                }else {
                                    yesButton.setVisibility(View.VISIBLE);
                                }
                                int ttsButtonId = getResources().getIdentifier("ttsButton" + i, "id", getPackageName());
                                Button ttsButton = findViewById(ttsButtonId);
                                if (hourString.equals("")) {
                                    ttsButton.setVisibility(View.INVISIBLE);
                                }else {
                                    ttsButton.setVisibility(View.VISIBLE);
                                }
                                int backgroundTextViewId = getResources().getIdentifier("backgroundTextView" + i, "id", getPackageName());
                                TextView backgroundTextView = findViewById(backgroundTextViewId);
                                if (hourString.equals("")) {
                                    backgroundTextView.setVisibility(View.INVISIBLE);
                                }else {
                                    backgroundTextView.setVisibility(View.VISIBLE);
                                }
                                int cardViewId = getResources().getIdentifier("cardView" + i, "id", getPackageName());
                                CardView cardView = findViewById(cardViewId);
                                if (hourString.equals("")) {
                                    cardView.setVisibility(View.INVISIBLE);
                                }else {
                                    cardView.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Long day = resultDocuments.get(i - 1).getLong("day");
                                String dayString = (day != null) ? String.valueOf(day) : "";

                                Long hour = resultDocuments.get(i - 1).getLong("hour");
                                String hourString = (hour != null) ? String.valueOf(hour) : "";

                                Long minute = resultDocuments.get(i - 1).getLong("minute");
                                String minuteString = (minute != null) ? String.valueOf(minute) : "";

                                String title = resultDocuments.get(i - 1).getString("title");
                                String titleString = (title != null) ? title : "";

                                String content = resultDocuments.get(i - 1).getString("content");
                                String contentString = (content != null) ? content : "";

                                String daytime = "";
                                if (dayString.equals("0")) {
                                    daytime = "오늘";
                                } else if (dayString.equals("1")) {
                                    daytime = "내일";
                                }  else if (dayString.equals("2")) {
                                    daytime = "모래";
                                } else if (dayString.equals("3")) {
                                    daytime = "사흘";
                                } else if (dayString.equals("4")) {
                                    daytime = "나흘";
                                }

                                String time = daytime + " " + hourString + "시 " + minuteString + "분";

                                int titleTextViewId = getResources().getIdentifier("titleTextView" + i, "id", getPackageName());
                                TextView titleTextView = findViewById(titleTextViewId);
                                if (titleTextView != null) {
                                    titleTextView.setText("");
                                } else {
                                    titleTextView.setText("");
                                }
                                int timeTextViewId = getResources().getIdentifier("timeTextView" + i, "id", getPackageName());
                                TextView timeTextView = findViewById(timeTextViewId);
                                if (timeTextView != null) {
                                    timeTextView.setText("");
                                }else {
                                    titleTextView.setText("");
                                }
                                int contentTextViewId = getResources().getIdentifier("contentTextView" + i, "id", getPackageName());
                                TextView contentTextView = findViewById(contentTextViewId);
                                if (contentTextView != null) {
                                    contentTextView.setText("");
                                }else {
                                    titleTextView.setText("");
                                }
                                int yesButtonId = getResources().getIdentifier("yesButton" + i, "id", getPackageName());
                                Button yesButton = findViewById(yesButtonId);
                                if (hourString.equals("")) {
                                    yesButton.setVisibility(View.INVISIBLE);
                                }else {
                                    yesButton.setVisibility(View.INVISIBLE);
                                }
                                int ttsButtonId = getResources().getIdentifier("ttsButton" + i, "id", getPackageName());
                                Button ttsButton = findViewById(ttsButtonId);
                                if (hourString.equals("")) {
                                    ttsButton.setVisibility(View.INVISIBLE);
                                }else {
                                    ttsButton.setVisibility(View.INVISIBLE);
                                }
                                int backgroundTextViewId = getResources().getIdentifier("backgroundTextView" + i, "id", getPackageName());
                                TextView backgroundTextView = findViewById(backgroundTextViewId);
                                if (hourString.equals("")) {
                                    backgroundTextView.setVisibility(View.INVISIBLE);
                                }else {
                                    backgroundTextView.setVisibility(View.INVISIBLE);
                                }
                                int cardViewId = getResources().getIdentifier("cardView" + i, "id", getPackageName());
                                CardView cardView = findViewById(cardViewId);
                                if (hourString.equals("")) {
                                    cardView.setVisibility(View.INVISIBLE);
                                }else {
                                    cardView.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "데이터 가져오기 실패", e);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
