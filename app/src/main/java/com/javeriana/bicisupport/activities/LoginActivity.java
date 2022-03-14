package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        loginButton = findViewById(R.id.loginButton);
        register = findViewById(R.id.register_option);

        register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, UserRegisterActivity.class)));
        loginButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, HomeActivity.class)));
    }
}