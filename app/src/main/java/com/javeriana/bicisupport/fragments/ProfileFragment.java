package com.javeriana.bicisupport.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.activities.LoginActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button logout, biciDetails, novedades;
    Button editProfile;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

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

        biciDetails = root.findViewById(R.id.bici_details);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        biciDetails.setOnClickListener(view -> {
            DetailBikeFragment detailBikeFragment = new DetailBikeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, detailBikeFragment);
            fragmentTransaction.commit();
        });

        logout = root.findViewById(R.id.logout);
        biciDetails = root.findViewById(R.id.bici_details);
        novedades = root.findViewById(R.id.novedades);
        editProfile = root.findViewById(R.id.edit_profile);

        logout.setOnClickListener(view -> {
            editor.remove("token");
            editor.remove("localId");

            editor.commit();

            startActivity(new Intent(view.getContext(), LoginActivity.class));
        });
        biciDetails.setOnClickListener(view -> {
            DetailBikeFragment fragment = new DetailBikeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
        });
        novedades.setOnClickListener(view -> {
            IncidentsListFragment fragment = new IncidentsListFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
        });

        editProfile.setOnClickListener(View -> {
            ModifyProfileFragment modifyProfileFragment = new ModifyProfileFragment();
            FragmentTransaction fragmentTransaction = ((HomeActivity) getContext()).getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, modifyProfileFragment);
            fragmentTransaction.commit();
        });


        return root;

    }
}