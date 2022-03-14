package com.javeriana.bicisupport.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.javeriana.bicisupport.R;

import java.util.Objects;

public class UserRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}