package com.javeriana.bicisupport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.LoginActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button logout, bici_details;

    public ProfileFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Cuenta</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        bici_details = root.findViewById(R.id.bici_details);

        bici_details.setOnClickListener(view -> {
            DetailBikeFragment detailBikeFragment = new DetailBikeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, detailBikeFragment);
            fragmentTransaction.commit();
        });

        logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(view -> startActivity(new Intent(view.getContext(), LoginActivity.class)));

        return root;
    }
}