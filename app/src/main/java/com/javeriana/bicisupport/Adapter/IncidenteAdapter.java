package com.javeriana.bicisupport.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.javeriana.bicisupport.DetalleIncidente;
import com.javeriana.bicisupport.models.Incident;
import com.javeriana.bicisupport.R;

import java.util.ArrayList;


public class IncidenteAdapter extends ArrayAdapter<Incident> {
    private ArrayList<Incident> incidents;
    private Context context;

    public IncidenteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Incident> objects) {
        super(context, resource, objects);
        this.incidents = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.novedadrow, parent,false);
        }

        TextView tvTitulo = convertView.findViewById(R.id.lnovedad);
        TextView tvTNovedad = convertView.findViewById(R.id.ltipoasistencia);
        TextView tvDireccion = convertView.findViewById(R.id.ldireccion);
        TextView etFecha = convertView.findViewById(R.id.lfecha);
        EditText etdetalle = convertView.findViewById(R.id.ldetalle);
        Button detalle = convertView.findViewById(R.id.lbtndetalle);
        Button calificacion = convertView.findViewById(R.id.lbtncalificacion);

        tvTitulo.setText("Novedad N. "+String.valueOf(incidents.get(position).getNumero()));
        tvTNovedad.setText(incidents.get(position).getNovedad());
        tvDireccion.setText(incidents.get(position).getDireccion());
        etFecha.setText(incidents.get(position).getFecha());
        etdetalle.setText(incidents.get(position).getDetalle());

        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetalleIncidente.class).putExtra("incidente", incidents.get(position)));
            }
        });



      return convertView;
    }


}
