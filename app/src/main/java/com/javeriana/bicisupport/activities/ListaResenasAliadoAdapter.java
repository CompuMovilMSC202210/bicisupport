package com.javeriana.bicisupport.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.javeriana.bicisupport.R;

public class ListaResenasAliadoAdapter extends ArrayAdapter<String> {


    private Activity context;
    private String[] nombreUsuarios;
    private String[] fecharesenas;
    private String[] descripcionResena;



    public ListaResenasAliadoAdapter(Activity context, String[] nombreUsuarios,
                                     String[]fecharesenas, String[] descripcionResena) {
        super(context, R.layout.activity_lista_resenas_aliado, nombreUsuarios);

        this.context = context;
        this.nombreUsuarios = nombreUsuarios;
        this.fecharesenas = fecharesenas;
        this.descripcionResena = descripcionResena;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.vista_resena_lista, null, true);

        TextView nombreUsuarioText = rowView.findViewById(R.id.listaResenas_nombreUsuario);
        TextView fechaResenaText = rowView.findViewById(R.id.listaResenas_fechaResena);
        TextView descripcionResenaText = rowView.findViewById(R.id.listaResenas_descripcionResena);

        nombreUsuarioText.setText(nombreUsuarios[position]);
        fechaResenaText.setText(fecharesenas[position]);
        descripcionResenaText.setText(descripcionResena[position]);

        return rowView;
    }
}
