package com.javeriana.bicisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DetalleIncidente extends AppCompatActivity {
    Incidente incidente;
    TextView novedad, fecha, direccion, detalle, empresa, titulo;
    EditText costos, servicios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidente);
        Bundle obtenerobjeto = getIntent().getExtras();

        if (obtenerobjeto != null){
            incidente =(Incidente) obtenerobjeto.getSerializable("incidente");
            titulo = findViewById(R.id.nNumeronovedad);
            novedad = findViewById(R.id.ntipo);
            fecha = findViewById(R.id.nfecha);
            direccion = findViewById(R.id.ndireccion);
            detalle = findViewById(R.id.ndetalle);
            empresa = findViewById(R.id.nempresa);
            costos = findViewById(R.id.ncosto);
            servicios = findViewById(R.id.nservicios);

            titulo.setText("Novedad N."+incidente.getNumero());
            novedad.setText(incidente.getNovedad());
            fecha.setText(incidente.getFecha());
            direccion.setText(incidente.getDireccion());
            detalle.setText(incidente.getDetalle());
            empresa.setText(incidente.empresa);
            costos.setText(incidente.getCostos().toString());
            servicios.setText(incidente.getServiciosp());
        }
    }
}