package com.oldcare.capstonedesign.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oldcare.capstonedesign.databinding.FragmentMasterCommunityBinding;

public class MasterCommunityFragment extends Fragment {

    private FragmentMasterCommunityBinding binding;

    public MasterCommunityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMasterCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
