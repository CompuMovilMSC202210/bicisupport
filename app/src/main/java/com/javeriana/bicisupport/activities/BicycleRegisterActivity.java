package com.javeriana.bicisupport.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.requests.BiciRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BicycleRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button finish;
    Spinner brandSpinner, typeSpinner, colorSpinner;
    EditText modelEditText;

    String token, localId, name, username, brand, type, color, imageUrl;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        prefs = getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        finish = findViewById(R.id.finishButton);
        brandSpinner = findViewById(R.id.spinner_brand);
        typeSpinner = findViewById(R.id.spinner_type);
        colorSpinner = findViewById(R.id.spinner_color);
        modelEditText = findViewById(R.id.edit_text_model);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_list);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.spinner_list);
        colorSpinner.setAdapter(colorAdapter);

        token = getIntent().getStringExtra("token");
        localId = getIntent().getStringExtra("localId");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("user");
        imageUrl = getIntent().getStringExtra("imageUrl");

        brandSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);

        finish.setOnClickListener(view -> this.checkData());
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

    private void checkData() {
        if (!brand.isEmpty() && !color.isEmpty() && !type.isEmpty() && !modelEditText.getText().toString().isEmpty())
            this.updateUser();
    }

    private void updateUser() {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(this);

        int model = Integer.parseInt(modelEditText.getText().toString());

        BiciRequest biciRequest = new BiciRequest(brand, model, color, type);

        Gson gson = new Gson();

        String biciJson = gson.toJson(biciRequest);

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    Intent intent = new Intent(BicycleRegisterActivity.this, HomeActivity.class);
                    UserRequest userRequest = new Gson().fromJson(response, UserRequest.class);

                    editor.putString("token", token);
                    editor.putString("localId", userRequest.getLocalId());

                    editor.commit();

                    startActivity(intent);
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(this)
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