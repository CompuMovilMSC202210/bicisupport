package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class DetailBikeFragment extends Fragment {


    public DetailBikeFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_bike, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Detalle de tip</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        return root;
    }
}