package com.javeriana.bicisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.javeriana.bicisupport.Adapter.IncidenteAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListaIncidentes extends AppCompatActivity {
    ListView listadoIncidentes;
    ArrayList<Incidente> incidentes;
    IncidenteAdapter iadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_incidentes);

        listadoIncidentes = findViewById(R.id.listadoIncidentes);
        cargarJSON();
        try {
            llenarIncidentes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        iadapter = new IncidenteAdapter(this,R.layout.novedadrow,incidentes);
        listadoIncidentes.setAdapter(iadapter);
    }

    public String cargarJSON(){
        String json;
        try{
            InputStream is = this.getAssets().open("incidentes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json= new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void llenarIncidentes() throws JSONException {
        JSONObject json = new JSONObject(cargarJSON());
        JSONArray arrayIncidentes = json.getJSONArray("incidentes");
        incidentes = new ArrayList<Incidente>();
        for (int i=0; i < arrayIncidentes.length(); i++){
            JSONObject objeto = arrayIncidentes.getJSONObject(i);
            incidentes.add(new Incidente(objeto.getInt("numero"),objeto.getString("tipo"),objeto.getString("direccion"), objeto.getString("fecha"), objeto.getString("detalle"),objeto.getString("empresa"),objeto.getString("sprestados"),objeto.getDouble("costos")));
        }
    }
}