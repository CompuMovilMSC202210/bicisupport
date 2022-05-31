package com.javeriana.bicisupport.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.fragments.ChatFragment;
import com.javeriana.bicisupport.fragments.ListaAliadosFragment;
import com.javeriana.bicisupport.fragments.MapFragment;
import com.javeriana.bicisupport.fragments.ProfileFragment;
import com.javeriana.bicisupport.fragments.TipsFragment;
import com.javeriana.bicisupport.models.requests.UserRequest;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ImageButton assistant, help, profile, allies, home;
    private MapView map;
    private IMapController mapController;

    SharedPreferences prefs;
    RequestQueue requestQueue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefs = getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        String localId = prefs.getString("localId", "");

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String updatedToken = instanceIdResult.getToken();
                updateUser(localId, updatedToken);
            }
        });

        assistant = findViewById(R.id.assistantButton);
        help = findViewById(R.id.helpButton);
        profile = findViewById(R.id.personButton);
        allies = findViewById(R.id.alliesButton);
        home = findViewById(R.id.homeButton);

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
        home.setOnClickListener(view -> {
            MapFragment mapFragment = new MapFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, mapFragment).commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_navigation, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView, mapFragment).commit();
    }

    public void onClickMap(MenuItem item) {
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView, mapFragment).commit();
    }

    private void updateUser(String localId, String chatToken) {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    UserRequest userRequest = new Gson().fromJson(response, UserRequest.class);
                    Log.i("Updated user", userRequest.toString());
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(getApplicationContext())
                            .setMessage("Ha ocurrido un error actualizando los datos")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cloudToken", chatToken);
                return params;
            }
        };

        requestQueue.add(request);
    }
}