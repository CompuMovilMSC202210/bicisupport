package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.javeriana.bicisupport.adapters.IncidenteAdapter;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.Incident;
import com.javeriana.bicisupport.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncidentsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncidentsListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ListView listadoIncidentes;
    ArrayList<Incident> incidents;
    IncidenteAdapter iadapter;

    public IncidentsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncidentsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncidentsListFragment newInstance(String param1, String param2) {
        IncidentsListFragment fragment = new IncidentsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_incidents_list, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Novedades</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        listadoIncidentes = root.findViewById(R.id.listadoIncidentes);
        try {
            InputStream is = getActivity().getAssets().open("incidentes.json");
            Utils.loadJson(is);
            populateIncidents();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        iadapter = new IncidenteAdapter(root.getContext(), R.layout.novedadrow, incidents);
        listadoIncidentes.setAdapter(iadapter);

        return root;
    }

    public void populateIncidents() throws JSONException, IOException {
        InputStream is = getActivity().getAssets().open("incidentes.json");
        JSONObject json = new JSONObject(Objects.requireNonNull(Utils.loadJson(is)));
        JSONArray arrayIncidentes = json.getJSONArray("incidentes");
        incidents = new ArrayList<>();
        for (int i = 0; i < arrayIncidentes.length(); i++) {
            JSONObject objeto = arrayIncidentes.getJSONObject(i);
            incidents.add(new Incident(objeto.getInt("numero"), objeto.getString("tipo"), objeto.getString("direccion"), objeto.getString("fecha"), objeto.getString("detalle"), objeto.getString("empresa"), objeto.getString("sprestados"), objeto.getDouble("costos")));
        }
    }
}