package com.oldcare.capstonedesign.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oldcare.capstonedesign.databinding.FragmentMasterLoadingBinding;

public class MasterLoadingFragment extends Fragment {

    private FragmentMasterLoadingBinding binding;

    public MasterLoadingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMasterLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
