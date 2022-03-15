package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.Incident;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncidentDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncidentDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Incident incident;
    TextView novedad, fecha, direccion, detalle, empresa, titulo;
    EditText costos, servicios;

    public IncidentDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncidentDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncidentDetailFragment newInstance(String param1, String param2) {
        IncidentDetailFragment fragment = new IncidentDetailFragment();
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
        View root = inflater.inflate(R.layout.fragment_incident_detail, container, false);
        
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Detalle novedad</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        Bundle obtenerobjeto = getActivity().getIntent().getExtras();

        if (obtenerobjeto != null){
            incident =(Incident) obtenerobjeto.getSerializable("incidente");
            titulo = root.findViewById(R.id.nNumeronovedad);
            novedad = root.findViewById(R.id.ntipo);
            fecha = root.findViewById(R.id.nfecha);
            direccion = root.findViewById(R.id.ndireccion);
            detalle = root.findViewById(R.id.ndetalle);
            empresa = root.findViewById(R.id.nempresa);
            costos = root.findViewById(R.id.ncosto);
            servicios = root.findViewById(R.id.nservicios);

            titulo.setText("Novedad N."+ incident.getNumero());
            novedad.setText(incident.getNovedad());
            fecha.setText(incident.getFecha());
            direccion.setText(incident.getDireccion());
            detalle.setText(incident.getDetalle());
            empresa.setText(incident.getEmpresa());
            costos.setText(incident.getCostos().toString());
            servicios.setText(incident.getServiciosp());
        }

        return inflater.inflate(R.layout.fragment_incident_detail, container, false);
    }
}