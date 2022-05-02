package com.javeriana.bicisupport.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.javeriana.bicisupport.models.requests.CredentialsRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;
import com.javeriana.bicisupport.models.responses.LoginResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRegisterActivity extends AppCompatActivity {

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private final String[] permissionsList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Button next, galleryButton, cameraButton;
    TextView login;
    EditText nameEditText, userEditText, emailEditText, passwordEditText, repeatPasswordEditText;
    ImageView resultImage;

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
        setContentView(R.layout.activity_user_register);

        Objects.requireNonNull(getSupportActionBar()).hide();

        getMultiplePermissions.launch(permissionsList);

        enabledCamera = false;
        enabledGallery = false;

        resultImage = findViewById(R.id.profile_register_picture);
        cameraButton = findViewById(R.id.camera_button);
        galleryButton = findViewById(R.id.gallery_button);

        next = findViewById(R.id.btn_register_next);
        login = findViewById(R.id.login_option);

        nameEditText = findViewById(R.id.edit_text_name);
        userEditText = findViewById(R.id.edit_text_user);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        repeatPasswordEditText = findViewById(R.id.edit_text_repeat_password);

        next.setOnClickListener(view -> this.checkRegisterData());
        login.setOnClickListener(view -> startActivity(new Intent(UserRegisterActivity.this, LoginActivity.class)));
        cameraButton.setOnClickListener(view -> {
            enabledCamera = true;
            checkCameraPermission();
        });
        galleryButton.setOnClickListener(view -> {
            enabledGallery = true;
            checkGalleryPermission();
        });
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

    private void checkRegisterData() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String user = userEditText.getText().toString();

        if (!password.equals(repeatPassword)) {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Las contraseñas no son iguales")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                    .create();
            alert.setTitle("Error en el registro");
            alert.show();
        }

        if (!email.isEmpty() && !password.isEmpty() && !repeatPassword.isEmpty()
                && !name.isEmpty() && !user.isEmpty())
            this.register();
        else
            Toast.makeText(UserRegisterActivity.this, "Faltan datos", Toast.LENGTH_LONG).show();

    }

    private void register() {
        String baseRegisterUrl = "https://bici-support-api.herokuapp.com/api/v1/auth/register";
        requestQueue = Volley.newRequestQueue(this);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String imageString = "";

        CredentialsRequest credentials = new CredentialsRequest(email, password);

        String name = nameEditText.getText().toString();
        String user = userEditText.getText().toString();

        UserRequest userRequest;

        if (Objects.nonNull(resultImage.getDrawable())) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) resultImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            userRequest = new UserRequest(imageString, name, user);
        } else
            userRequest = new UserRequest(name, user);

        Gson gson = new Gson();

        String credentialsJson = gson.toJson(credentials);
        String userJson = gson.toJson(userRequest);

        String finalImageString = imageString;
        StringRequest request = new StringRequest(Request.Method.POST, baseRegisterUrl,
                response -> {
                    Intent intent = new Intent(UserRegisterActivity.this, BicycleRegisterActivity.class);
                    LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);

                    intent.putExtra("token", loginResponse.getToken());
                    intent.putExtra("localId", loginResponse.getLocalId());
                    intent.putExtra("name", name);
                    intent.putExtra("user", user);
                    intent.putExtra("imageUrl", finalImageString);

                    startActivity(intent);
                },
                error -> {
                    String errorMessage = handleRegisterAnswer(error.networkResponse.statusCode);
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setMessage(errorMessage)
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("credentials", credentialsJson);
                params.put("user", userJson);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private String handleRegisterAnswer(int status) {
        if (status == 409)
            return "Este email ya está en uso";
        if (status == 405)
            return "Este proyecto tiene el inicio de sesión con contraseña deshabilitado";
        if (status == 429)
            return "El usuario ha sido bloqueado debido a actividad inusual.";

        return "Error desconocido";
    }
}