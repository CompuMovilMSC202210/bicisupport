package com.javeriana.bicisupport.fragments;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.javeriana.bicisupport.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.Objects;

public class MapFragment extends Fragment {
    private MapView map;
    private IMapController mapController;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

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

        sensorManager = (SensorManager) ctx.getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        View root = inflater.inflate(R.layout.fragment_map,container,false);

        map = new MapView(ctx);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(20);
        GeoPoint startPoint = new GeoPoint(4.6269924,-74.0651919);
        mapController.setCenter(startPoint);
        ((ConstraintLayout) root.findViewById(R.id.mapLayout)).addView(map);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Inicio</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (map != null) {
                    if (sensorEvent.values[0] < 5000) {
                        map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                    } else
                        map.getOverlayManager().getTilesOverlay().setColorFilter(null);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

}