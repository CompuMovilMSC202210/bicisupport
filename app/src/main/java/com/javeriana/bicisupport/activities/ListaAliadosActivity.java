package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.javeriana.bicisupport.R;

public class ListaAliadosActivity extends AppCompatActivity {

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
            "descripcion 5"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aliados);

        ListaAliadosAdapter adapterListaAliados= new ListaAliadosAdapter(this, nombreAliados, direcciones, descripciones);
        listaAliados = findViewById(R.id.listaAliados);
        listaAliados.setAdapter(adapterListaAliados);

    }
}