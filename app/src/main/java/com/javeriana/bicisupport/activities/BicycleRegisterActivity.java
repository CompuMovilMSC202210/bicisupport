package com.javeriana.bicisupport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.javeriana.bicisupport.R;

public class BicycleRegisterActivity extends AppCompatActivity {

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_register);

        finish = findViewById(R.id.finishButton);

        finish.setOnClickListener(view -> startActivity(new Intent(BicycleRegisterActivity.this, HomeActivity.class)));
    }
}