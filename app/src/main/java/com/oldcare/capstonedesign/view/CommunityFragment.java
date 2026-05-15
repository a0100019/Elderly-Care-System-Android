package com.oldcare.capstonedesign.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.oldcare.capstonedesign.controller.RecordActivity;
import com.oldcare.capstonedesign.controller.StepCounterActivity;
import com.oldcare.capstonedesign.databinding.FragmentCommunityBinding;
import com.oldcare.capstonedesign.controller.LocationActivity;
import com.oldcare.capstonedesign.controller.TermsofUseofLocationbasedServicesnActivity;

public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);

        binding.memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
            }
        });

        binding.mapButton.setOnClickListener(view1 -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            SharedPreferences preferences = requireContext().getSharedPreferences("user_preferences", MODE_PRIVATE);
            String uid = preferences.getString("uid", "");
            db.collection("Users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int agreeValue = documentSnapshot.getLong("agree") != null ?
                                    documentSnapshot.getLong("agree").intValue() : 0;

                            if (agreeValue == 1) {
                                Intent intent = new Intent(getActivity(), LocationActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), TermsofUseofLocationbasedServicesnActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        });

        binding.stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StepCounterActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
