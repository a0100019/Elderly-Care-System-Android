package com.oldcare.capstonedesign.controller;
nimport com.oldcare.capstonedesign.model.Person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oldcare.capstonedesign.databinding.ActivityRecordBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RecordActivity extends AppCompatActivity {

    private ActivityRecordBinding binding;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(new RecyclerViewAdapter());

        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String oldMan = preferences.getString("oldMan", "");

        private ArrayList<Person> telephoneBook = new ArrayList<>();

        public RecyclerViewAdapter() {
            firestore.collection("Users").document(oldMan).collection("message").addSnapshotListener((querySnapshot, firebaseFirestoreException) -> {
                telephoneBook.clear();

                for (QueryDocumentSnapshot snapshot : querySnapshot) {
                    Person item = snapshot.toObject(Person.class);
                    telephoneBook.add(item);
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TextView titleView = holder.itemView.findViewById(R.id.titleTextView);
            TextView contentView = holder.itemView.findViewById(R.id.contentTextView);
            TextView timeView = holder.itemView.findViewById(R.id.timeTextView);

            titleView.setText(telephoneBook.get(position).getTitle());
            contentView.setText(telephoneBook.get(position).getContent());

            String timeValue = telephoneBook.get(position).getTime();
            if (timeValue != null && !timeValue.isEmpty()) {
                timeView.setText(timeValue);
            } else {
                timeView.setText("미수행");
            }
        }

        @Override
        public int getItemCount() {
            return telephoneBook.size();
        }
    }
}
