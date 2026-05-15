package com.oldcare.capstonedesign.controller;
nimport com.oldcare.capstonedesign.model.Hospital;
import com.oldcare.capstonedesign.model.Institution;
import com.oldcare.capstonedesign.view.SimpleTextAdapter;
import com.oldcare.capstonedesign.view.SimpleTextItemTouchHelperCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.oldcare.capstonedesign.databinding.ActivityCenterBinding;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity implements SimpleTextAdapter.OnStartDragViewHolderListener {

    private ActivityCenterBinding binding;
    private ItemTouchHelper itemTouchHelper;
    private List<String> centerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        List<Institution> receivedAgencyList = (List<Institution>) intent.getSerializableExtra("centers");
        List<Hospital> receivedHospitalList = (List<Hospital>) intent.getSerializableExtra("hospitals");
        if (receivedAgencyList != null) {
            for (Institution institution : receivedAgencyList) {
                String sb = "기관명 :" + institution.name + "\n" +
                        "전화번호 :" + institution.phonenumber + "\n" +
                        "주소 :" + institution.address;
                centerList.add(sb);
            }
            for (Hospital Hospital : receivedHospitalList) {
                String sb = "기관명 :" + Hospital.placeName + "\n" +
                        "전화번호 :" + Hospital.phone + "\n" +
                        "주소 :" + Hospital.roadAddressName;
                centerList.add(sb);
            }
        }

        binding.centerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.centerList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SimpleTextAdapter adapter = new SimpleTextAdapter(centerList, this);
        binding.centerList.setAdapter(adapter);

        itemTouchHelper = new ItemTouchHelper(new SimpleTextItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(binding.centerList);
    }

    @Override
    public void onStartDragViewHolder(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
