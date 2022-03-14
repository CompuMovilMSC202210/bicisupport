package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.ListaAliadosAdapter;

import java.util.Objects;

public class DetalleAliadoFragment extends Fragment {

    public DetalleAliadoFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detalle_aliado, container, false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Aliados</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        return root;

    }
}