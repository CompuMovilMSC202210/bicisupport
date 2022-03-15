package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class TipsFragment extends Fragment {

    Button btn_tip_detail_2, btn_tip_detail_1;

    public TipsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tips, container, false);

        btn_tip_detail_1 = root.findViewById(R.id.btn_tip_detail_1);

        btn_tip_detail_1.setOnClickListener(view -> {
            DetailTipFragment detailTipFragment = new DetailTipFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, detailTipFragment);
            fragmentTransaction.commit();
        });

        btn_tip_detail_2 = root.findViewById(R.id.btn_tip_detail_2);

        btn_tip_detail_2.setOnClickListener(view -> {
            DetailTipFragment detailTipFragment = new DetailTipFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, detailTipFragment);
            fragmentTransaction.commit();
        });

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Tips</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        return root;
    }
}