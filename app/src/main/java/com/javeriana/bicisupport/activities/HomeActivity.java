package com.javeriana.bicisupport.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.fragments.ProfileFragment;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ImageButton assistant, help, profile, agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));

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

            fragmentTransaction.replace(R.id.fragmentContainerView, profileFragment).commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_navigation, menu);
        return true;
    }

    public void onClickMap(MenuItem item) {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView, profileFragment).commit();
    }
}