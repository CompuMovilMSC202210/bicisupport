package com.javeriana.bicisupport.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.javeriana.bicisupport.models.requests.CredentialsRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;
import com.javeriana.bicisupport.models.responses.LoginResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRegisterActivity extends AppCompatActivity {

    Button next;
    TextView login;
    EditText nameEditText, userEditText, emailEditText, passwordEditText, repeatPasswordEditText;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        next = findViewById(R.id.btn_register_next);
        login = findViewById(R.id.login_option);

        nameEditText = findViewById(R.id.edit_text_name);
        userEditText = findViewById(R.id.edit_text_user);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        repeatPasswordEditText = findViewById(R.id.edit_text_repeat_password);

        next.setOnClickListener(view -> this.checkRegisterData());
        login.setOnClickListener(view -> startActivity(new Intent(UserRegisterActivity.this, LoginActivity.class)));
    }

    private void checkRegisterData() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String user = userEditText.getText().toString();

        if (!password.equals(repeatPassword)) {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Las contraseñas no son iguales")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                    .create();
            alert.setTitle("Error en el registro");
            alert.show();
        }

        if (!email.isEmpty() && !password.isEmpty() && !repeatPassword.isEmpty()
                && !name.isEmpty() && !user.isEmpty())
            this.register();
        else
            Toast.makeText(UserRegisterActivity.this, "Faltan datos", Toast.LENGTH_LONG).show();

    }

    private void register() {
        String baseRegisterUrl = "https://bici-support-api.herokuapp.com/api/v1/auth/register";
        requestQueue = Volley.newRequestQueue(this);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        CredentialsRequest credentials = new CredentialsRequest(email, password);

        String name = nameEditText.getText().toString();
        String user = userEditText.getText().toString();

        UserRequest userRequest = new UserRequest(name, user);

        Gson gson = new Gson();

        String credentialsJson = gson.toJson(credentials);
        String userJson = gson.toJson(userRequest);

        StringRequest request = new StringRequest(Request.Method.POST, baseRegisterUrl,
                response -> {
                    Intent intent = new Intent(UserRegisterActivity.this, BicycleRegisterActivity.class);
                    LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);

                    intent.putExtra("token", loginResponse.getToken());
                    intent.putExtra("localId", loginResponse.getLocalId());
                    intent.putExtra("name", name);
                    intent.putExtra("user", user);

                    startActivity(intent);
                },
                error -> {
                    String errorMessage = handleRegisterAnswer(error.networkResponse.statusCode);
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setMessage(errorMessage)
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("credentials", credentialsJson);
                params.put("user", userJson);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private String handleRegisterAnswer(int status) {
        if (status == 409)
            return "Este email ya está en uso";
        if (status == 405)
            return "Este proyecto tiene el inicio de sesión con contraseña deshabilitado";
        if (status == 429)
            return "El usuario ha sido bloqueado debido a actividad inusual.";

        return "Error desconocido";
    }
}