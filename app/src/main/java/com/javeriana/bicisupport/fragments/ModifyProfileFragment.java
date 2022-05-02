package com.javeriana.bicisupport.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.models.requests.UserRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModifyProfileFragment extends Fragment {

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private final String[] permissionsList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    EditText userEditText, nameEditText;
    Button saveButton, cameraButton, galleryButton;
    ImageView profilePicture;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String localId, imageUrl;
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
                    Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
    );

    private final ActivityResultLauncher<String> getCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted)
                    this.takePhoto(getView());
                else
                    Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
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


    public ModifyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modify_profile, container, false);

        userEditText = root.findViewById(R.id.ModificarCuenta_editTextUser);
        nameEditText = root.findViewById(R.id.ModificarCuenta_editTextNombre);
        saveButton = root.findViewById(R.id.modificarCuenta_saveButton);
        profilePicture = root.findViewById(R.id.ModificarCuenta_imageView);
        cameraButton = root.findViewById(R.id.details_camera_button);
        galleryButton = root.findViewById(R.id.details_gallery_button);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        getMultiplePermissions.launch(permissionsList);

        enabledCamera = false;
        enabledGallery = false;

        imageUrl = prefs.getString("imageUrl", "");

        if (!imageUrl.equals("")) {
            byte[] imageBytes;
            imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profilePicture.setImageBitmap(decodedImage);
        }

        localId = prefs.getString("localId", "");
        userEditText.setText(prefs.getString("userUser", ""));
        nameEditText.setText(prefs.getString("userName", ""));

        saveButton.setOnClickListener(this::checkData);

        cameraButton.setOnClickListener(view -> {
            enabledCamera = true;
            checkCameraPermission(view);
        });
        galleryButton.setOnClickListener(view -> {
            enabledGallery = true;
            checkGalleryPermission(view);
        });

        return root;
    }

    private void checkGalleryPermission(View view) {
        if (ContextCompat.checkSelfPermission(view.getContext(), READ_EXTERNAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED)
            selectGalleryImage();
        else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_EXTERNAL_STORAGE_PERMISSION)) {
            AlertDialog alert = new AlertDialog.Builder(view.getContext())
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

    private void checkCameraPermission(View view) {
        if (ContextCompat.checkSelfPermission(view.getContext(), CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED)
            takePhoto(view);
        else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), CAMERA_PERMISSION)) {
            AlertDialog alert = new AlertDialog.Builder(view.getContext())
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

    private void takePhoto(View view) {
        if (enabledCamera) {
            File file = new File(getActivity().getFilesDir(), "picFromCamera");
            uriCamera = FileProvider.getUriForFile(view.getContext(),
                    getActivity().getApplicationContext().getPackageName(), file);
            getCameraContent.launch(uriCamera);
        }
    }

    private void loadIntoImage(Uri uri) {
        Glide.with(this).load(uri).into(profilePicture);
    }

    private void checkData(View view) {
        if (!userEditText.getText().toString().isEmpty()
                && !nameEditText.getText().toString().isEmpty())
            this.updateUser(view);
    }

    private void updateUser(View view) {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(view.getContext());

        if (Objects.nonNull(profilePicture.getDrawable())) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageUrl = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    UserRequest userRequest = new Gson().fromJson(response, UserRequest.class);

                    editor.putString("userName", userRequest.getName());
                    editor.putString("userUser", userRequest.getUser());

                    editor.commit();

                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
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
                params.put("user", userEditText.getText().toString());
                params.put("name", nameEditText.getText().toString());
                if (!imageUrl.equals(""))
                    params.put("imageUrl", imageUrl);

                return params;
            }
        };

        requestQueue.add(request);
    }
}