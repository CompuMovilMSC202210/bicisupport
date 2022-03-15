package com.javeriana.bicisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EncuestaSatisfaccionChat extends AppCompatActivity {
    Button go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_satisfaccion_chat);
        go =findViewById(R.id.enviarEncuesta);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), ListaIncidentes.class);
                //startActivity(intent);
            }
        });
    }


}