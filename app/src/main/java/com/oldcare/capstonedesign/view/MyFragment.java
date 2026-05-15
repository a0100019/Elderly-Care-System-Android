package com.oldcare.capstonedesign.view;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.oldcare.capstonedesign.controller.MasterServiceActivity;
import com.oldcare.capstonedesign.controller.WarningActivity;
import com.oldcare.capstonedesign.databinding.FragmentMyBinding;
import com.oldcare.capstonedesign.controller.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyFragment extends Fragment {

    private FragmentMyBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyBinding.inflate(inflater, container, false);

        signOut();
        deleteID();
        nicknameEvent();

        binding.warningChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WarningActivity.class);
                startActivity(intent);
            }
        });

        binding.stopServiceChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MasterServiceActivity.class);
                intent.putExtra("key", "second");
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void nicknameEvent() {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("user_preferences", MODE_PRIVATE);
        String nickName = preferences.getString("nickName", "");
        String masterNumber = preferences.getString("masterNumber", "");
        String oldNumber = preferences.getString("oldNumber", "");
        binding.masterNumberTextView.setText(masterNumber);
        binding.oldNumberTextView.setText(oldNumber);
        binding.nicknameTextView.setText(nickName);

        binding.nicknameChangeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                editText.setHint("혈액형과 같이 중요한 정보를 작성해주세요. 어르신의 핸드폰에 표시됩니다.");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("어르신 정보 변경");
                builder.setView(editText);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String enteredText = editText.getText().toString();

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            String uid = currentUser.getUid();
                            DocumentReference userRef = db.collection("Users").document(uid);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("nickname", enteredText);

                            userRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("nickName", enteredText);
                                            editor.apply();

                                            Toast.makeText(getActivity(), "어르신 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                            binding.nicknameTextView.setText(enteredText);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("취소", null);

                builder.create().show();
            }
        });
    }

    private void signOut() {
        binding.signOutChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void deleteID() {
        mAuth = FirebaseAuth.getInstance();

        binding.deleteIDChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                                Toast.makeText(getActivity(), "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else {
                                                if (task.getException().getMessage().contains("requires recent authentication")) {
                                                    Toast.makeText(getActivity(), "로그인이 오래되었습니다. 재 로그인 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "계정 삭제 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
