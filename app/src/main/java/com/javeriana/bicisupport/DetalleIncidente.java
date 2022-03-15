package com.javeriana.bicisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.javeriana.bicisupport.models.Incident;

public class DetalleIncidente extends AppCompatActivity {
    Incident incident;
    TextView novedad, fecha, direccion, detalle, empresa, titulo;
    EditText costos, servicios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidente);
        Bundle obtenerobjeto = getIntent().getExtras();

        if (obtenerobjeto != null){
            incident =(Incident) obtenerobjeto.getSerializable("incidente");
            titulo = findViewById(R.id.nNumeronovedad);
            novedad = findViewById(R.id.ntipo);
            fecha = findViewById(R.id.nfecha);
            direccion = findViewById(R.id.ndireccion);
            detalle = findViewById(R.id.ndetalle);
            empresa = findViewById(R.id.nempresa);
            costos = findViewById(R.id.ncosto);
            servicios = findViewById(R.id.nservicios);

            titulo.setText("Novedad N."+ incident.getNumero());
            novedad.setText(incident.getNovedad());
            fecha.setText(incident.getFecha());
            direccion.setText(incident.getDireccion());
            detalle.setText(incident.getDetalle());
            empresa.setText(incident.getEmpresa());
            costos.setText(incident.getCostos().toString());
            servicios.setText(incident.getServiciosp());
        }
    }
}