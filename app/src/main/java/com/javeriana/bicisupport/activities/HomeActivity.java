package com.javeriana.bicisupport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.javeriana.bicisupport.R;

public class HomeActivity extends AppCompatActivity {

    ImageView assistant, help, profile, agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        assistant = findViewById(R.id.assistantImage);
        help = findViewById(R.id.helpImage);
        profile = findViewById(R.id.personImage);
        agreement = findViewById(R.id.agreementImage);

        agreement.setOnClickListener(view -> System.out.println("Agreement clicked"));
        help.setOnClickListener(view -> System.out.println("Help clicked"));
        assistant.setOnClickListener(view -> System.out.println("Assistant clicked"));
        profile.setOnClickListener(view -> System.out.println("Profile clicked"));
    }
}