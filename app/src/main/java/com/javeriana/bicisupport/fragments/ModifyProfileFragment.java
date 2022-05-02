package com.javeriana.bicisupport.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.BicycleRegisterActivity;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.models.requests.BiciRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;

import java.util.HashMap;
import java.util.Map;

public class ModifyProfileFragment extends Fragment {

    EditText userEditText, nameEditText;
    Button saveButton;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String localId;

    RequestQueue requestQueue;

    public ModifyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modify_profile, container, false);

        userEditText = root.findViewById(R.id.ModificarCuenta_editTextUser);
        nameEditText = root.findViewById(R.id.ModificarCuenta_editTextNombre);
        saveButton = root.findViewById(R.id.modificarCuenta_saveButton);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        localId = prefs.getString("localId", "");
        userEditText.setText(prefs.getString("userUser", ""));
        nameEditText.setText(prefs.getString("userName", ""));

        saveButton.setOnClickListener(this::checkData);

        return root;
    }

    private void checkData(View view) {
        if (!userEditText.getText().toString().isEmpty()
                && !nameEditText.getText().toString().isEmpty())
            this.updateUser(view);
    }

    private void updateUser(View view) {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    UserRequest userRequest = new Gson().fromJson(response, UserRequest.class);

                    editor.putString("userName", userRequest.getName());
                    editor.putString("userUser", userRequest.getUser());

                    editor.commit();

                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
                            .setMessage("Ha ocurrido un error actualizando los datos")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", userEditText.getText().toString());
                params.put("name", nameEditText.getText().toString());

                return params;
            }
        };

        requestQueue.add(request);
    }
}