package com.javeriana.bicisupport.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.ListaResenasAliadoAdapter;

import java.util.Objects;

public class ListaResenasAliadoFragment extends Fragment {

    ListView listaResenas;

    String[] nombreUsuarios = {
            "Por Usuario 1", " Por Usuario 2",
            "Por Usuario 3", "Por Usuario 4",
            "Por Usuario 5", "Por Usuario 6",
            "Por Usuario 7", "Por Usuario 8",
            "Por Usuario 9", "Por Usuario 10"
    };
    String[] fechaResenas = {
            "2022/02/05", "2022/03/06",
            "2022/04/07", "2022/05/08",
            "2022/06/09", "2022/06/09",
            "2022/06/09", "2022/06/09",
            "2022/06/09", "2022/06/09",
    };
    String[] descripcionResenas = {
            "Resena 1", "Resena 2",
            "Resena 3", "Resena 4",
            "Resena 5", "Resena 6",
            "Resena 7", "Resena 8",
            "Resena 9", "Resena 10",
    };

    public ListaResenasAliadoFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lista_resenas_aliado, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Reseñas</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        ListaResenasAliadoAdapter adapterListaResenas = new ListaResenasAliadoAdapter(getActivity(), nombreUsuarios, fechaResenas, descripcionResenas);
        listaResenas = root.findViewById(R.id.listaResenasAliado);
        listaResenas.setAdapter(adapterListaResenas);

        return root;

    }

}