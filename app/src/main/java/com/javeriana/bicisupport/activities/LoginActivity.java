package com.javeriana.bicisupport.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.responses.LoginResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView register;
    EditText emailText, passwordText;

    RequestQueue requestQueue;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        loginButton = findViewById(R.id.loginButton);
        register = findViewById(R.id.register_option);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        prefs = getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        String token = prefs.getString("token", "");
        String localId = prefs.getString("localId", "");

        if (!token.equals("") && !localId.equals(""))
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, UserRegisterActivity.class)));
        loginButton.setOnClickListener(view -> {
            if (!emailText.getText().toString().isEmpty()
                    && !passwordText.getText().toString().isEmpty())
                this.login();
            else
                Toast.makeText(LoginActivity.this, "Correo y Contraseña obligatorios", Toast.LENGTH_LONG).show();
        });
    }

    private void login() {
        String baseLoginUrl = "https://bici-support-api.herokuapp.com/api/v1/auth/login";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, baseLoginUrl,
                response -> {
                    LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);

                    editor.putString("token", loginResponse.getToken());
                    editor.putString("localId", loginResponse.getLocalId());

                    editor.commit();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                },
                error -> {
                    String errorMessage = handleLoginAnswer(error.networkResponse.statusCode);
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setMessage(errorMessage)
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el inicio de sesión");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailText.getText().toString());
                params.put("password", passwordText.getText().toString());

                return params;
            }
        };

        requestQueue.add(request);
    }

    private String handleLoginAnswer(int status) {
        if (status == 404)
            return "Usuario no encontrado";
        if (status == 401)
            return "Usuario o contraseña no validos";
        if (status == 403)
            return "La cuenta se encuentra deshabilitada";

        return "Error desconocido";
    }
}