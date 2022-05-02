package com.javeriana.bicisupport.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.javeriana.bicisupport.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailBikeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner brandSpinner, typeSpinner, colorSpinner;
    EditText modelEditText;
    Button save;

    String localId, brand, type, color;
    int model;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    RequestQueue requestQueue;

    public DetailBikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_bike, container, false);

        brandSpinner = root.findViewById(R.id.spinner_brand);
        typeSpinner = root.findViewById(R.id.spinner_type);
        colorSpinner = root.findViewById(R.id.spinner_color);
        modelEditText = root.findViewById(R.id.edit_text_model);
        save = root.findViewById(R.id.btn_save_bici_details);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        localId = prefs.getString("localId", "");
        brand = prefs.getString("biciBrand", "");
        type = prefs.getString("biciType", "");
        color = prefs.getString("biciColor", "");
        model = prefs.getInt("biciModel", 0);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_list);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.spinner_list);
        colorSpinner.setAdapter(colorAdapter);

        brandSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Detalle de bici</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        modelEditText.setText(String.valueOf(model));

        int brandPosition = brandAdapter.getPosition(brand);
        brandSpinner.setSelection(brandPosition);

        int colorPosition = colorAdapter.getPosition(color);
        colorSpinner.setSelection(colorPosition);

        int typePosition = typeAdapter.getPosition(type);
        typeSpinner.setSelection(typePosition);

        save.setOnClickListener(this::checkData);

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_brand:
                brand = String.valueOf(adapterView.getItemAtPosition(i));
                break;
            case R.id.spinner_color:
                color = String.valueOf(adapterView.getItemAtPosition(i));
                break;
            case R.id.spinner_type:
                type = String.valueOf(adapterView.getItemAtPosition(i));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void checkData(View view) {
        if (!brand.isEmpty() && !color.isEmpty() && !type.isEmpty() && !modelEditText.getText().toString().isEmpty())
            this.updateUser(view);
    }

    private void updateUser(View view) {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(view.getContext());

        model = Integer.parseInt(modelEditText.getText().toString());

        BiciRequest biciRequest = new BiciRequest(brand, model, color, type);

        Gson gson = new Gson();

        String biciJson = gson.toJson(biciRequest);

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    editor.putString("biciType", type);
                    editor.putString("biciBrand", brand);
                    editor.putString("biciColor", color);
                    editor.putInt("biciModel", model);

                    editor.commit();

                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
                            .setMessage("Ha ocurrido un error registrando la data de la bici")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bici", biciJson);

                return params;
            }
        };

        requestQueue.add(request);
    }
}