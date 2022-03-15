package com.javeriana.bicisupport.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.fragments.ChatFragment;
import com.javeriana.bicisupport.fragments.MapFragment;
import com.javeriana.bicisupport.fragments.ListaAliadosFragment;
import com.javeriana.bicisupport.fragments.ProfileFragment;
import com.javeriana.bicisupport.fragments.TipsFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ImageButton assistant, help, profile, allies;
    private MapView map;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        assistant = findViewById(R.id.assistantButton);
        help = findViewById(R.id.helpButton);
        profile = findViewById(R.id.personButton);
        allies = findViewById(R.id.alliesButton);

        allies.setOnClickListener(view -> {
            ListaAliadosFragment aliadosFragment = new ListaAliadosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, aliadosFragment);
            fragmentTransaction.commit();
        });
        help.setOnClickListener(view -> {
            TipsFragment tipsFragment = new TipsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, tipsFragment);
            fragmentTransaction.commit();
        });
        assistant.setOnClickListener(view -> {
            ChatFragment chatFragment = new ChatFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, chatFragment).commit();
        });
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
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView, mapFragment).commit();
    }
}