package com.javeriana.bicisupport.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.javeriana.bicisupport.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Objects;

public class MapFragment extends Fragment {
    private MapView map;
    private IMapController mapController;

    Geocoder mGeocoder;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    double latitude;
    double longitude;
    private GeoPoint startPoint;
    MyLocationNewOverlay myLocationOverlay;

    //Obtener permiso de localizacion
    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (!result) {
                        Log.i("MAPS","no hay permiso de localizacion");
                        Toast.makeText(getActivity().getApplicationContext(), "Permiso de localizacion denegado", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    } else {
                        startLocationUpdates();
                    }
                }
            }
    );

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        View root = inflater.inflate(R.layout.fragment_map,container,false);

        map = new MapView(ctx);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(20);
        //GeoPoint startPoint = new GeoPoint(4.6269924,-74.0651919);

        ((ConstraintLayout) root.findViewById(R.id.mapLayout)).addView(map);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Inicio</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        //init geocoder
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mGeocoder = new Geocoder(container.getContext());

        //overlay para localizacion actual
        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        GpsMyLocationProvider provider = new GpsMyLocationProvider(getActivity());
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        myLocationOverlay = new MyLocationNewOverlay(provider, map);
        myLocationOverlay.enableFollowLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                Log.d("MyTag", String.format("First location fix: %s", myLocationOverlay.getLastFix()));
            }
        });

        //overlay para eventos
        map.getOverlayManager().add(myLocationOverlay);
        map.getOverlays().add(createOverlayEvents());

        //sucripcion cambios de localizacion
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    startPoint = new GeoPoint(latitude, longitude);
                    //writeJSONObject();
                }
            }
        };
        mapController.setCenter(startPoint);
        startLocationUpdates();

        return root;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    startPoint = new GeoPoint(latitude, longitude);
                }
            });
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private MapEventsOverlay createOverlayEvents(){
        MapEventsOverlay overlayEventos = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                //longPressOnMap(p);
                return true;
            }
        });
        return overlayEventos;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        //sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(this.startPoint);
        myLocationOverlay.enableMyLocation();

    }
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        //sensorManager.unregisterListener(lightListener);
        myLocationOverlay.disableMyLocation();
    }

}