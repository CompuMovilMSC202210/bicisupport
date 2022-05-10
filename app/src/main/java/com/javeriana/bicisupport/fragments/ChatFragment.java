package com.javeriana.bicisupport.fragments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.MensajeAdapter;
import com.javeriana.bicisupport.models.Mensaje;

import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Mensaje> mensajes= new ArrayList<>();
    EditText mensaje;
    Button btnsend;
    RecyclerView listamensaje;

    public ChatFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatEvaluationFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Asistencia</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.aliados);
        imageView.setColorFilter(Color.parseColor("#00239E"));
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100,100, Gravity.START | Gravity.CENTER_VERTICAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        mensaje = root.findViewById(R.id.cmensajec);
        btnsend =  root.findViewById(R.id.send);
        listamensaje = root.findViewById(R.id.lmensajesc);

        MensajeAdapter msgadapter = new MensajeAdapter(mensajes,this.getContext() );
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(root.getContext());
        listamensaje.setLayoutManager(layoutManager);
        listamensaje.setItemAnimator(new DefaultItemAnimator());
        listamensaje.setAdapter(msgadapter);

        btnsend.setOnClickListener(view -> {
            if(mensaje.getText().toString().length() > 0){
                mensajes.add(new Mensaje(mensaje.getText().toString(), "Usuario"));
            }
            mensaje.setText("");
        });

        return root;
    }



    }
