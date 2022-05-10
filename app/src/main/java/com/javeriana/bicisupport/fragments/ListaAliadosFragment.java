package com.javeriana.bicisupport.fragments;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.ListaAliadosAdapter;

import java.util.Objects;

public class ListaAliadosFragment extends Fragment {

    ListView listaAliados;

    String[] nombreAliados ={
            "aliado 1","aliado 2",
            "aliado 3","aliado 4",
            "aliado 5",
    };

    String[] direcciones ={
            "direccion 1","direccion 2",
            "direccion 3","direccion 4",
            "direccion 5",
    };


    String[] descripciones={
            "descripcion 1", "descripcion 2",
            "descripcion 3", "descripcion 4",
            "descripcion 5 "
    };

    public ListaAliadosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lista_aliados, container, false);

        //Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Aliados</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Aliados</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.aliados);
        imageView.setColorFilter(Color.parseColor("#00239E"));
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100,100, Gravity.START | Gravity.CENTER_VERTICAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        ListaAliadosAdapter adapterListaAliados= new ListaAliadosAdapter(getActivity(), nombreAliados, direcciones, descripciones);
        listaAliados = root.findViewById(R.id.listaAliados);
        listaAliados.setAdapter(adapterListaAliados);

        return root;

    }
}