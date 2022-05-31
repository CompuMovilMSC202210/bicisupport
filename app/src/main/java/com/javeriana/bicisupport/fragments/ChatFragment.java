package com.javeriana.bicisupport.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.MensajeAdapter;
import com.javeriana.bicisupport.models.Mensaje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String cloudToken;
    String destinyName;

    SharedPreferences prefs;
    RequestQueue requestQueue;

    private ArrayList<Mensaje> mensajes = new ArrayList<>();
    EditText mensaje;
    Button btnsend;
    RecyclerView listamensaje;

    public ChatFragment() {
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
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100, 100, Gravity.START | Gravity.CENTER_VERTICAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        mensaje = root.findViewById(R.id.cmensajec);
        btnsend = root.findViewById(R.id.send);
        listamensaje = root.findViewById(R.id.lmensajesc);

        Bundle args = this.getArguments();
        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);

        if (args != null) {
            cloudToken = args.getString("cloudToken");
            destinyName = args.getString("destinyName");
        }

        MensajeAdapter msgAdapter = new MensajeAdapter(mensajes, this.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        listamensaje.setLayoutManager(layoutManager);
        listamensaje.setItemAnimator(new DefaultItemAnimator());
        listamensaje.setAdapter(msgAdapter);

        btnsend.setOnClickListener(view -> {
            if (mensaje.getText().toString().length() > 0) {
                String name = prefs.getString("userName", "");
                mensajes.add(new Mensaje(mensaje.getText().toString(), name));
                sendNotification(mensaje.getText().toString(), root);
            }
            mensaje.setText("");
        });

        return root;
    }

    private void sendNotification(String message, View view) {
        String name = prefs.getString("userName", "");

        String baseSendNotificationUrl = "https://bici-support-api.herokuapp.com/api/v1/notifications";
        requestQueue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, baseSendNotificationUrl,
                response -> {
                    Log.i("Response", response);
                },
                error -> Log.e("Notificacion", error.getMessage())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", cloudToken);
                params.put("title", String.format("Nuevo mensaje de %s", name));
                params.put("msg", message);
                return params;
            }
        };

        requestQueue.add(request);
    }

}
