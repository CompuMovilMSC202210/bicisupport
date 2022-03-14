package com.javeriana.bicisupport.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.fragments.DetalleAliadoFragment;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.fragments.ListaResenasAliadoFragment;

public class ListaAliadosAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] nombreAliados;
    private String[] direccionAliados;
    private String[] descripcionesAliados;

    public ListaAliadosAdapter(Activity context, String[] nombreAliados,
                               String[] direccionAliados, String[] descripcionesAliados) {

        super(context, R.layout.fragment_lista_aliados, nombreAliados);

        this.context = context;
        this.nombreAliados = nombreAliados;
        this.direccionAliados = direccionAliados;
        this.descripcionesAliados = descripcionesAliados;

    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.vista_aliado_lista, null,true);

        TextView nombreAliadoText =  rowView.findViewById(R.id.listaResenas_nombreUsuario);
        TextView direccionAliadoText =  rowView.findViewById(R.id.listaAliados_direccionAliado);
        TextView descripcionAliadoText =  rowView.findViewById(R.id.listaResenas_descripcionResena);
        Button botonDetalle = rowView.findViewById(R.id.listaAliado_Detalle);
        Button botonResenas = rowView.findViewById(R.id.listaAliado_Resenas);

        nombreAliadoText.setText(nombreAliados[position]);
        direccionAliadoText.setText(direccionAliados[position]);
        descripcionAliadoText.setText(descripcionesAliados[position]);

        botonDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleAliadoFragment detalleAliadoFragment = new DetalleAliadoFragment();
                FragmentTransaction fragmentTransaction =  ((HomeActivity)context).getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, detalleAliadoFragment);
                fragmentTransaction.commit();
            }
        });

        botonResenas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaResenasAliadoFragment listaResenasAliadoFragment= new ListaResenasAliadoFragment();
                FragmentTransaction fragmentTransaction = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, listaResenasAliadoFragment);
                fragmentTransaction.commit();
            }
        });

        return rowView;


    }
}
