package com.javeriana.bicisupport.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.fragments.ChatEvaluationFragment;
import com.javeriana.bicisupport.fragments.IncidentDetailFragment;
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

        tvTitulo.setText(String.format("Novedad N. %s", incidents.get(position).getNumero()));
        tvTNovedad.setText(incidents.get(position).getNovedad());
        tvDireccion.setText(incidents.get(position).getDireccion());
        etFecha.setText(incidents.get(position).getFecha());
        etdetalle.setText(incidents.get(position).getDetalle());

        detalle.setOnClickListener(view -> {
            Bundle datos = new Bundle();
            datos.putSerializable("incidente",incidents.get(position));
            IncidentDetailFragment fragment = new IncidentDetailFragment();
            fragment.setArguments(datos);
            FragmentTransaction fragmentTransaction = ((HomeActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
            // context.startActivity(new Intent(context, DetalleIncidente.class).putExtra("incidente", incidents.get(position)));
        });

        calificacion.setOnClickListener(view -> {
            ChatEvaluationFragment fragment = new ChatEvaluationFragment();
            FragmentTransaction fragmentTransaction = ((HomeActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
        });
      return convertView;
    }


}
