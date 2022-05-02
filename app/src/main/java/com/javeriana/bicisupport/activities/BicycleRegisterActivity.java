package com.javeriana.bicisupport.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.requests.BiciRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BicycleRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private final String[] permissionsList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Button finish, cameraButton, galleryButton;
    Spinner brandSpinner, typeSpinner, colorSpinner;
    EditText modelEditText;
    ImageView resultImage;

    String token, localId, name, username, brand, type, color, imageUrl;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Uri uriCamera;
    boolean enabledCamera, enabledGallery;

    RequestQueue requestQueue;

    private final ActivityResultLauncher<String[]> getMultiplePermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissionsStatusMap -> {
                if (!permissionsStatusMap.containsValue(false))
                    Log.i("PERMISSIONS", "All granted");
                else
                    Log.i("PERMISSIONS", "At least one denied");
            }
    );

    private final ActivityResultLauncher<String> getGalleryPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted)
                    this.selectGalleryImage();
                else
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
    );

    private final ActivityResultLauncher<String> getCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted)
                    this.takePhoto();
                else
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
    );

    private final ActivityResultLauncher<String> getGalleryContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::loadIntoImage
    );


    private final ActivityResultLauncher<Uri> getCameraContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> loadIntoImage(uriCamera)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        prefs = getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        getMultiplePermissions.launch(permissionsList);

        enabledCamera = false;
        enabledGallery = false;

        finish = findViewById(R.id.finishButton);
        brandSpinner = findViewById(R.id.spinner_brand);
        typeSpinner = findViewById(R.id.spinner_type);
        colorSpinner = findViewById(R.id.spinner_color);
        modelEditText = findViewById(R.id.edit_text_model);
        galleryButton = findViewById(R.id.bici_register_gallery_button);
        cameraButton = findViewById(R.id.bici_register_camera_button);
        resultImage = findViewById(R.id.bici_register_picture);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(this,
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_list);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.spinner_list);
        colorSpinner.setAdapter(colorAdapter);

        token = getIntent().getStringExtra("token");
        localId = getIntent().getStringExtra("localId");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("user");
        imageUrl = getIntent().getStringExtra("imageUrl");

        brandSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);

        finish.setOnClickListener(view -> this.checkData());
        cameraButton.setOnClickListener(view -> {
            enabledCamera = true;
            checkCameraPermission();
        });
        galleryButton.setOnClickListener(view -> {
            enabledGallery = true;
            checkGalleryPermission();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_brand:
                brand = String.valueOf(adapterView.getItemAtPosition(i));
                break;
            case R.id.spinner_color:
                color = String.valueOf(adapterView.getItemAtPosition(i));
                break;
            case R.id.spinner_type:
                type = String.valueOf(adapterView.getItemAtPosition(i));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED)
            selectGalleryImage();
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE_PERMISSION)) {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Se requiere el permiso de seleccionar fotos de la galeria")
                    .setCancelable(false)
                    .setPositiveButton("Ok, dar permisos",
                            (dialog, which) -> getGalleryPermission.launch(READ_EXTERNAL_STORAGE_PERMISSION))
                    .setNegativeButton("Denegar", (dialog, which) -> dialog.cancel())
                    .create();
            alert.setTitle("Permiso necesario");
            alert.show();
        }
    }

    private void selectGalleryImage() {
        if (enabledGallery) {
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            getGalleryContent.launch("image/*");
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED)
            takePhoto();
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION)) {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Se requiere el permiso de camara")
                    .setCancelable(false)
                    .setPositiveButton("Ok, dar permisos",
                            (dialog, which) -> getCameraPermission.launch(CAMERA_PERMISSION))
                    .setNegativeButton("Denegar", (dialog, which) -> dialog.cancel())
                    .create();
            alert.setTitle("Permiso necesario");
            alert.show();
        }
    }

    private void takePhoto() {
        if (enabledCamera) {
            File file = new File(getFilesDir(), "picFromCamera");
            uriCamera = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName(), file);
            getCameraContent.launch(uriCamera);
        }
    }

    private void loadIntoImage(Uri uri) {
        Glide.with(this).load(uri).into(resultImage);
    }

    private void checkData() {
        if (!brand.isEmpty() && !color.isEmpty() && !type.isEmpty() && !modelEditText.getText().toString().isEmpty())
            this.updateUser();
    }

    private void updateUser() {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(this);

        int model = Integer.parseInt(modelEditText.getText().toString());
        String imageString = "";

        BiciRequest biciRequest;

        if (Objects.nonNull(resultImage.getDrawable())) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) resultImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            biciRequest = new BiciRequest(imageString, brand, model, color, type);
        } else
            biciRequest = new BiciRequest(brand, model, color, type);

        Gson gson = new Gson();

        String biciJson = gson.toJson(biciRequest);

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    Intent intent = new Intent(BicycleRegisterActivity.this, HomeActivity.class);
                    UserRequest userRequest = new Gson().fromJson(response, UserRequest.class);

                    editor.putString("token", token);
                    editor.putString("localId", userRequest.getLocalId());

                    editor.commit();

                    startActivity(intent);
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setMessage("Ha ocurrido un error registrando la data de la bici")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bici", biciJson);

                return params;
            }
        };

        requestQueue.add(request);
    }
}