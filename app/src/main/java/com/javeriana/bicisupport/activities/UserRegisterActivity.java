package com.javeriana.bicisupport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class UserRegisterActivity extends AppCompatActivity {

    Button next;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        next = findViewById(R.id.nextButton);
        login = findViewById(R.id.login_option);

        next.setOnClickListener(view -> startActivity(new Intent(UserRegisterActivity.this, BicycleRegisterActivity.class)));
        login.setOnClickListener(view -> startActivity(new Intent(UserRegisterActivity.this, LoginActivity.class)));
    }
}