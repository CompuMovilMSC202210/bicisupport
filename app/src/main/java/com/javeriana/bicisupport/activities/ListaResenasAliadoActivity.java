package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.ListaResenasAliadoAdapter;

public class ListaResenasAliadoActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_resenas_aliado);

        ListaResenasAliadoAdapter adapterListaResenas = new ListaResenasAliadoAdapter(this, nombreUsuarios, fechaResenas, descripcionResenas);
        listaResenas = findViewById(R.id.listaResenasAliado);
        listaResenas.setAdapter(adapterListaResenas);
    }
}