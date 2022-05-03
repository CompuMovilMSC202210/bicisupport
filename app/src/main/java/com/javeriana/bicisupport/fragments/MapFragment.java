package com.javeriana.bicisupport.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.MyLocation;
import com.javeriana.bicisupport.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    double destinationLatitude;
    double destinationLongitude;
    private GeoPoint startPoint;
    MyLocationNewOverlay myLocationOverlay;
    Button botonLocalizar;
    Button botonCapas;
    Button botonDirecciones;
    boolean settingsOK = false;

    RoadManager roadManager;
    Polyline roadOverlay;
    FolderOverlay poiMarkers;
    FolderOverlay directionMarkers;
    FolderOverlay historicoMarkers;
    Marker longPressedMarker;

    private JSONArray localizaciones = new JSONArray();
    private List<MyLocation> historicoLocalizaciones;

    //Obtener permiso de localizacion
    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (!result) {
                        Log.i("MAPS","no hay permiso de localizacion");
                        Toast.makeText(getActivity().getApplicationContext(), "Permiso de localizacion denegado", Toast.LENGTH_LONG).show();
                        setDefaultLocation();
                    } else {
                        Log.i("MAPS","Permiso de localizacion concedido");
                        startLocationUpdates();
                    }
                }
            }
    );

    ActivityResultLauncher<IntentSenderRequest> getLocationSettings =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.i("MAPS", "Result from settings: "+result.getResultCode());
                            if(result.getResultCode() == getActivity().RESULT_OK){
                                settingsOK = true;
                                startLocationUpdates();
                            }else{
                                Toast.makeText(getContext(), "GPS apagado", Toast.LENGTH_LONG).show();
                                settingsOK = false;
                            }
                        }
                    });

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

        botonLocalizar = root.findViewById(R.id.buttonlocalizar);
        botonCapas = root.findViewById(R.id.buttonLayers);
        botonDirecciones = root.findViewById(R.id.buttonDirecciones);

        map = new MapView(ctx);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(20);

        ((FrameLayout) root.findViewById(R.id.mapLayout)).addView(map);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Inicio</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        //init geocoder
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mGeocoder = new Geocoder(container.getContext());

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
                    writeJSONObject();
                }
            }
        };

        //overlay e inicializacion para localizacion actual (revisar gps encendido y permisos)
        getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        checkLocationSettings();
        GpsMyLocationProvider provider = new GpsMyLocationProvider(getActivity());
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        myLocationOverlay = new MyLocationNewOverlay(provider, map);

        //definir icono de localizacion actual
        Drawable currentDraw = ResourcesCompat.getDrawable(getResources(), R.drawable.bikelocation, null);
        Bitmap currentIcon = ((BitmapDrawable) currentDraw).getBitmap();
        myLocationOverlay.setDirectionArrow(currentIcon, currentIcon);
        myLocationOverlay.setPersonIcon(currentIcon);

        myLocationOverlay.enableFollowLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                Log.d("MyTag", String.format("First location fix: %s", myLocationOverlay.getLastFix()));
            }
        });

        //overlay para eventos
        map.getOverlayManager().add(myLocationOverlay);
        map.getOverlays().add(createOverlayEvents());

        mapController.setCenter(startPoint);

        //accion de boton para centrar en localizacion actual
        botonLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                checkLocationSettings();
                mapController.animateTo(startPoint);
            }
        });

        botonCapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.LEFT);
                popupMenu.getMenuInflater().inflate(R.menu.mapmenu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mostrarCais:
                                buscarCercanos("police");
                                Log.i("MAPS", "CAIS selecionado");
                                return true;
                            case R.id.mostrarParqueaderos:
                                buscarCercanos("bicycle_parking");
                                Log.i("MAPS", "parqueaderos selecionado");
                                return true;
                            case R.id.mostrarHistorico:
                                try {
                                    poblarHistoricos();
                                    mostrarHistoricos();
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i("MAPS", "Historico seleccionado");
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        botonDirecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                checkLocationSettings();
                if (settingsOK) {
                    GeoPoint start = new GeoPoint(myLocationOverlay.getMyLocation());
                    GeoPoint finish = new GeoPoint(destinationLatitude, destinationLongitude);
                    drawRoute(start, finish);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Por favor encienda el GPS", Toast.LENGTH_LONG).show();
                }

            }
        });

        //roadManager
        roadManager = new OSRMRoadManager(getActivity(), "ANDROID");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return root;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (settingsOK) {
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
            else
                setDefaultLocation();
        } else
            setDefaultLocation();
    }

    private void setDefaultLocation() {
        startPoint = new GeoPoint(4.6269924,-74.0651919);
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
                Log.i("MAP", "marcador?");
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                longPressOnMap(p);
                return true;
            }
        });
        return overlayEventos;
    }

    private Marker createMarker(GeoPoint p, String title, String desc, int iconID){
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (desc != null) marker.setSubDescription(desc);
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, getActivity().getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }

    private void longPressOnMap(GeoPoint p) {
        if(longPressedMarker!=null){
            map.getOverlays().remove(longPressedMarker);
        }
        try {

            List<Address> addresses = mGeocoder.getFromLocation(p.getLatitude(), p.getLongitude(), 2);
            Address addressResult = addresses.get(0);
            longPressedMarker = createMarker(p, addressResult.getAddressLine(0), null, R.drawable.ic_baseline_person_pin_circle_24);
            longPressedMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    mapController.animateTo(new GeoPoint(p.getLatitude(), p.getLongitude()));
                    marker.showInfoWindow();
                    destinationLatitude = p.getLatitude();
                    destinationLongitude = p.getLongitude();
                    botonDirecciones.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            map.getOverlays().add(longPressedMarker);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawRoute(GeoPoint start, GeoPoint finish){
        botonDirecciones.setVisibility(View.INVISIBLE);
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(start);
        routePoints.add(finish);
        ((OSRMRoadManager)roadManager).setMean(OSRMRoadManager.MEAN_BY_BIKE);
        Road road = roadManager.getRoad(routePoints);
        Log.i("MAPS", "Route length: "+road.mLength+" klm");
        Log.i("MAPS", "Duration: "+road.mDuration/60+" min");
        if(map!=null){
            if(roadOverlay!=null){
                map.getOverlays().remove(roadOverlay);
            }
            if (directionMarkers!=null) {
                map.getOverlays().remove(directionMarkers);
            }
            roadOverlay = RoadManager.buildRoadOverlay(road);
            roadOverlay.getOutlinePaint().setColor(Color.RED);
            roadOverlay.getOutlinePaint().setStrokeWidth(10);

            map.getOverlays().add(roadOverlay);

            directionMarkers = new FolderOverlay((getActivity()));
            Drawable nodeIcon = getResources().getDrawable(org.osmdroid.bonuspack.R.drawable.marker_default_focused_base);
            for (int i=0; i<road.mNodes.size(); i++){
                RoadNode node = road.mNodes.get(i);
                Marker nodeMarker = new Marker(map);
                nodeMarker.setPosition(node.mLocation);
                nodeMarker.setIcon(nodeIcon);
                nodeMarker.setTitle("Step "+i);
                nodeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                nodeMarker.setSnippet(node.mInstructions);
                nodeMarker.setSubDescription(Road.getLengthDurationText(getActivity(), node.mLength, node.mDuration));
                directionMarkers.add(nodeMarker);
            }
            map.getOverlays().add(directionMarkers);
        }
    }

    private void buscarCercanos(String facility) {
        if (poiMarkers != null) {
            map.getOverlays().remove(poiMarkers);
        }
        botonDirecciones.setVisibility(View.INVISIBLE);
        NominatimPOIProvider poiProvider = new NominatimPOIProvider("OSMBonusPackTutoUserAgent");
        ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint, facility, 50, 0.1);
        poiMarkers = new FolderOverlay(getActivity());
        Drawable poiIcon = getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default);
        for (POI poi:pois){
            Marker poiMarker = new Marker(map);
            poiMarker.setTitle(poi.mType);
            poiMarker.setSnippet(poi.mDescription);
            poiMarker.setPosition(poi.mLocation);
            poiMarker.setIcon(poiIcon);
            poiMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    mapController.animateTo(new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude()));
                    marker.showInfoWindow();
                    destinationLatitude = marker.getPosition().getLatitude();
                    destinationLongitude = marker.getPosition().getLongitude();
                    botonDirecciones.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            poiMarkers.add(poiMarker);
        }
        map.getOverlays().add(poiMarkers);
        mapController.zoomTo(15.0);
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i("MAP", "GPS is ON");
                settingsOK = true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (((ApiException) e).getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    IntentSenderRequest isr = new IntentSenderRequest.Builder(resolvable.getResolution()).build();
                    getLocationSettings.launch(isr);
                } else {
                    Log.i("MAP", "GPS is OFF");
                }
            }
        });
    }

    private void writeJSONObject() {
        MyLocation myLocation = new MyLocation();
        myLocation.setFecha(new Date(System.currentTimeMillis()));
        myLocation.setLatitud(myLocationOverlay.getMyLocation().getLatitude());
        myLocation.setLongitud(myLocationOverlay.getMyLocation().getLongitude());
        localizaciones.put(myLocation.toJSON());
        Writer output = null;
        String filename= "locations.json";
        try {
            File file = new File(getContext().getExternalFilesDir(null), filename);
            Log.i("LOCATION", "Ubicacion de archivo: "+file);
            output = new BufferedWriter(new FileWriter(file));
            output.write(localizaciones.toString());
            output.close();
        } catch (Exception e) {
            //Log error
            e.printStackTrace();
        }
    }

    private void poblarHistoricos() throws FileNotFoundException, JSONException {
        String filename= "locations.json";
        File fileRead = new File(getContext().getExternalFilesDir(null), filename);
        InputStream is = new FileInputStream(fileRead);
        JSONArray arrayLocalizaciones = new JSONArray(Objects.requireNonNull(Utils.loadJson(is)));
        historicoLocalizaciones = new ArrayList<>();
        for (int i = 0 ; i < arrayLocalizaciones.length(); i++) {
            JSONObject object = arrayLocalizaciones.getJSONObject(i);
            historicoLocalizaciones.add(
                    new MyLocation(
                            object.getDouble("latitud"),
                            object.getDouble("longitud")
                    ));
        }
    }

    private void mostrarHistoricos () throws IOException {

        if (poiMarkers != null) {
            map.getOverlays().remove(poiMarkers);
        }
        if (historicoMarkers != null) {
            map.getOverlays().remove(historicoMarkers);
        }

        historicoMarkers = new FolderOverlay(getActivity());
        Drawable poiIcon = getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default);
        for (MyLocation l : historicoLocalizaciones) {
            Address address = mGeocoder.getFromLocation(l.getLatitud(), l.getLongitud(), 1).get(0);
            GeoPoint p = new GeoPoint(address.getLatitude(), address.getLongitude());
            Marker historicoMarker = createMarker(p, address.getAddressLine(0), null, org.osmdroid.library.R.drawable.marker_default);
            historicoMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    mapController.animateTo(new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude()));
                    marker.showInfoWindow();
                    destinationLatitude = marker.getPosition().getLatitude();
                    destinationLongitude = marker.getPosition().getLongitude();
                    botonDirecciones.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            historicoMarkers.add(historicoMarker);
        }
        map.getOverlays().add(historicoMarkers);
        mapController.zoomTo(15.0);
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
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        map.onPause();
        //sensorManager.unregisterListener(lightListener);
        myLocationOverlay.disableMyLocation();
    }

}