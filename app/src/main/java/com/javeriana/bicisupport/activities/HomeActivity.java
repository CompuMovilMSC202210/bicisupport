package com.javeriana.bicisupport.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    ImageButton assistant, help, profile, agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        assistant = findViewById(R.id.assistantButton);
        help = findViewById(R.id.helpButton);
        profile = findViewById(R.id.personButton);
        agreement = findViewById(R.id.agreementButton);

        agreement.setOnClickListener(view -> System.out.println("Agreement clicked"));
        help.setOnClickListener(view -> System.out.println("Help clicked"));
        assistant.setOnClickListener(view -> System.out.println("Assistant clicked"));
        profile.setOnClickListener(view -> {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, profileFragment);
        });
    }
}