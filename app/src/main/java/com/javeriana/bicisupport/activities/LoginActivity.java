package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        loginButton = findViewById(R.id.loginButton);
    }
}