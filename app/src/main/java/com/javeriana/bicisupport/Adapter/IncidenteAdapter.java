package com.javeriana.bicisupport.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.javeriana.bicisupport.DetalleIncidente;
import com.javeriana.bicisupport.Incidente;
import com.javeriana.bicisupport.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class IncidenteAdapter extends ArrayAdapter<Incidente> {
    private ArrayList<Incidente> incidentes;
    private Context context;

    public IncidenteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Incidente> objects) {
        super(context, resource, objects);
        this.incidentes = objects;
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

        tvTitulo.setText("Novedad N. "+String.valueOf(incidentes.get(position).getNumero()));
        tvTNovedad.setText(incidentes.get(position).getNovedad());
        tvDireccion.setText(incidentes.get(position).getDireccion());
        etFecha.setText(incidentes.get(position).getFecha());
        etdetalle.setText(incidentes.get(position).getDetalle());

        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, DetalleIncidente.class).putExtra("incidente", incidentes.get(position)));
            }
        });



      return convertView;
    }


}
