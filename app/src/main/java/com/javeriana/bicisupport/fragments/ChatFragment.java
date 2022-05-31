package com.javeriana.bicisupport.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.adapters.MensajeAdapter;
import com.javeriana.bicisupport.models.BEMessage;
import com.javeriana.bicisupport.models.FirebaseChat;
import com.javeriana.bicisupport.models.FirebaseMessage;
import com.javeriana.bicisupport.models.LocationSharing;
import com.javeriana.bicisupport.models.Mensaje;
import com.javeriana.bicisupport.models.UserList;

import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PATH_LOCATIONSHARING = "/locationsharing";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String cloudToken;
    String destinyName;
    String userLocalId;
    String localId;
    private DatabaseReference mDatabase;

    SharedPreferences prefs;
    RequestQueue requestQueue;

    private ArrayList<Mensaje> mensajes = new ArrayList<>();
    EditText mensaje;
    Button btnsend;
    RecyclerView listamensaje;
    Button btnCompartirUbicacion;
    Button btnSeguirUbicacion;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    double latitude;
    double longitude;

    boolean settingsOK = false;
    boolean isLocationSharingActive =false;
    DatabaseReference locationSharingRef;
    LocationSharing locationSharing;

    //Obtener permiso de localizacion
    ActivityResultLauncher<String> getLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (!result) {
                        Log.i("MAPS", "no hay permiso de localizacion");
                        Toast.makeText(getActivity().getApplicationContext(), "Permiso de localizacion denegado", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("MAPS", "Permiso de localizacion concedido");
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
                            Log.i("MAPS", "Result from settings: " + result.getResultCode());
                            if (result.getResultCode() == getActivity().RESULT_OK) {
                                settingsOK = true;
                                startLocationUpdates();
                            } else {
                                Toast.makeText(getContext(), "GPS apagado", Toast.LENGTH_LONG).show();
                                settingsOK = false;
                            }
                        }
                    });

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatEvaluationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Asistencia</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.aliados);
        imageView.setColorFilter(Color.parseColor("#00239E"));
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100, 100, Gravity.START | Gravity.CENTER_VERTICAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        mensaje = root.findViewById(R.id.cmensajec);
        btnsend = root.findViewById(R.id.send);
        listamensaje = root.findViewById(R.id.lmensajesc);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        locationSharingRef = FirebaseDatabase.getInstance().getReference(PATH_LOCATIONSHARING);
        btnCompartirUbicacion = root.findViewById(R.id.botonCompartirUbicacion);
        btnSeguirUbicacion = root.findViewById(R.id.botonnseguirUbicacion);

        Bundle args = this.getArguments();
        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);

        if (args != null) {
            cloudToken = args.getString("cloudToken");
            destinyName = args.getString("destinyName");
            userLocalId = args.getString("userLocalId");
        }

        getMessages(root);

        MensajeAdapter msgAdapter = new MensajeAdapter(mensajes, this.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        listamensaje.setLayoutManager(layoutManager);
        listamensaje.setItemAnimator(new DefaultItemAnimator());
        listamensaje.setAdapter(msgAdapter);

        btnsend.setOnClickListener(view -> {
            if (mensaje.getText().toString().length() > 0) {
                String name = prefs.getString("userName", "");
                mensajes.add(new Mensaje(mensaje.getText().toString(), name));
                sendNotification(mensaje.getText().toString(), root);
            }
            mensaje.setText("");
        });

        //sucripcion cambios de localizacion
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("Chat", "Location update in the callback: " + location);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("Chat", "Estoy compartiendo mi ubicacion: " + location);
                    locationSharing = new LocationSharing();
                    locationSharing.setActive(true);
                    locationSharing.setLatitud(latitude);
                    locationSharing.setLongitud(longitude);
                    locationSharing.setUserSender(prefs.getString("localId", ""));
                    locationSharing.setUserReceiver(userLocalId);
                    locationSharingRef.setValue(locationSharing);
                    //startPoint = new GeoPoint(latitude, longitude);
                }
            }
        };

        btnCompartirUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = prefs.getString("userName", "");
                getLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                checkLocationSettings();
                btnSeguirUbicacion.setVisibility(View.VISIBLE);
                if (settingsOK) {
                mensaje.setText("Estoy compartiendo mi ubicacion: ir a mapa");
                mensajes.add(new Mensaje(mensaje.getText().toString(), name));
                sendNotification(mensaje.getText().toString(), root);
                mensaje.setText("");
                } else {
                    Toast.makeText(getContext(), "GPS apagado", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        listenChanges(this.getView());
    }

    private void sendNotification(String message, View view) {
        String name = prefs.getString("userName", "");
        localId = prefs.getString("localId", "");

        String baseSendNotificationUrl = "https://bici-support-api.herokuapp.com/api/v1/chat";
        requestQueue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, baseSendNotificationUrl,
                response -> {
                    Log.i("Response", response);
                },
                error -> Log.e("Notificacion", error.getMessage())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", cloudToken);
                params.put("title", String.format("Nuevo mensaje de %s", name));
                params.put("msg", message);
                params.put("sender", localId);
                params.put("destinatary", userLocalId);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void getMessages(View view) {
        String name = prefs.getString("userName", "");
        localId = prefs.getString("localId", "");

        String baseSendNotificationUrl = "https://bici-support-api.herokuapp.com/api/v1/chat/id";
        requestQueue = Volley.newRequestQueue(view.getContext());

        Type listType = new TypeToken<ArrayList<BEMessage>>() {
        }.getType();

        StringRequest request = new StringRequest(Request.Method.POST, baseSendNotificationUrl,
                response -> {
                    List<BEMessage> messages;
                    messages = new Gson().fromJson(response, listType);
                    mensajes = new ArrayList<>();
                    for (BEMessage m : messages) {
                        String emisor = m.getStatus().equals("received") ? destinyName : name;
                        Mensaje mensaje1 = new Mensaje(m.getMsg(), emisor);
                        mensajes.add(mensaje1);
                        MensajeAdapter msgAdapter = new MensajeAdapter(mensajes, this.getContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                        listamensaje.setLayoutManager(layoutManager);
                        listamensaje.setItemAnimator(new DefaultItemAnimator());
                        listamensaje.setAdapter(msgAdapter);
                    }
                },
                error -> Log.e("Notificacion", error.getMessage())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", localId);
                params.put("destinatary", userLocalId);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void listenChanges(View view) {
        String name = prefs.getString("userName", "");
        locationSharingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    Boolean sharingLocationActive = singleSnapshot.getValue(Boolean.class);
                    Log.i("CHAT", "activo: " + sharingLocationActive);
                    if (sharingLocationActive) {
                        btnSeguirUbicacion.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        btnSeguirUbicacion.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("/chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseChat chat = snapshot.getValue(FirebaseChat.class);
                        assert chat != null;
                        if ((chat.getSenderId().equals(localId) && chat.getDestinataryId().equals(userLocalId))
                                || (chat.getSenderId().equals(userLocalId) && chat.getDestinataryId().equals(localId))) {
                            if (!chat.getMessages().isEmpty()) {
                                mensajes = new ArrayList<>();
                                for (FirebaseMessage m : chat.getMessages()) {
                                    String emisor = m.getSender().equals(localId) ? name : destinyName;
                                    Mensaje mensaje1 = new Mensaje(m.getMsg(), emisor);
                                    mensajes.add(mensaje1);
                                    MensajeAdapter msgAdapter = new MensajeAdapter(mensajes, view.getContext());
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                                    listamensaje.setLayoutManager(layoutManager);
                                    listamensaje.setItemAnimator(new DefaultItemAnimator());
                                    listamensaje.setAdapter(msgAdapter);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("Error", "loadPost:onCancelled", databaseError.toException());
            }
        });
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

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (settingsOK) {
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            } else
                Log.i("Chat", "Error con localizacion");
                turnOffLocationSharing();
        } else
            Log.i("Chat", "Error con localizacion");
            turnOffLocationSharing();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void turnOffLocationSharing() {
        if (locationSharing == null) {
            locationSharing = new LocationSharing();
            locationSharing.setActive(false);
            locationSharingRef.setValue(locationSharing);
        } else {
            locationSharing.setActive(false);
            locationSharingRef.setValue(locationSharing);
        }
    }

}
