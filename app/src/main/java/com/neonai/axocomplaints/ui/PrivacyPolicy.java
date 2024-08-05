package com.neonai.axocomplaints.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neonai.axocomplaints.R;
import com.neonai.axocomplaints.WebViewActivity;
import com.neonai.axocomplaints.databinding.FragmentGalleryBinding;
import com.neonai.axocomplaints.databinding.FragmentPrivacyPolicyBinding;

public class PrivacyPolicy extends Fragment {


    private FragmentPrivacyPolicyBinding binding;
   /* String url = "https://tanajisawant.org/privacy";*/


    String url = "https://tsfc.axolotlsapps.in/privacy.html";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);

        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}