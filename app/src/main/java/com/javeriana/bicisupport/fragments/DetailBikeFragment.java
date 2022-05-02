package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class DetailBikeFragment extends Fragment {

    Spinner brandSpinner, typeSpinner, colorSpinner;
    EditText model;

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

        brandSpinner = root.findViewById(R.id.spinner_brand);
        typeSpinner = root.findViewById(R.id.spinner_type);
        colorSpinner = root.findViewById(R.id.spinner_color);
        model = root.findViewById(R.id.edit_text_model);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(colorAdapter);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Detalle de tip</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        return root;
    }
}