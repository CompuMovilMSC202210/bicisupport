package com.javeriana.bicisupport.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.javeriana.bicisupport.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiciDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiciDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner tipo, color, marca;

    public BiciDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BiciDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BiciDetailsFragment newInstance(String param1, String param2) {
        BiciDetailsFragment fragment = new BiciDetailsFragment();
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

        View root = inflater.inflate(R.layout.fragment_bici_details, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Detalled de la bici</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        tipo = root.findViewById(R.id.bstipo);
        color = root.findViewById(R.id.bscolor);
        marca = root.findViewById(R.id.bsmarca);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(root.getContext(),
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adaptermarca = ArrayAdapter.createFromResource(root.getContext(),
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adaptercolor = ArrayAdapter.createFromResource(root.getContext(),
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);


        tipo.setAdapter(adaptertipo);
        color.setAdapter(adaptercolor);
        marca.setAdapter(adaptermarca);

        return root;
    }
}