package com.javeriana.bicisupport.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.BicycleRegisterActivity;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.models.requests.BiciRequest;
import com.javeriana.bicisupport.models.requests.UserRequest;
import com.javeriana.bicisupport.utils.Utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailBikeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private final String[] permissionsList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Spinner brandSpinner, typeSpinner, colorSpinner;
    EditText modelEditText;
    Button save, cameraButton, galleryButton;
    ImageView profilePicture;

    String localId, brand, type, color, imageUrl;
    int model;
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

    public DetailBikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_bike, container, false);

        brandSpinner = root.findViewById(R.id.spinner_brand);
        typeSpinner = root.findViewById(R.id.spinner_type);
        colorSpinner = root.findViewById(R.id.spinner_color);
        modelEditText = root.findViewById(R.id.edit_text_model);
        save = root.findViewById(R.id.btn_save_bici_details);
        profilePicture = root.findViewById(R.id.bici_details_picture);
        galleryButton = root.findViewById(R.id.bici_details_gallery_button);
        cameraButton = root.findViewById(R.id.bici_details_camera_button);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        getMultiplePermissions.launch(permissionsList);

        enabledCamera = false;
        enabledGallery = false;

        localId = prefs.getString("localId", "");
        brand = prefs.getString("biciBrand", "");
        type = prefs.getString("biciType", "");
        color = prefs.getString("biciColor", "");
        imageUrl = prefs.getString("biciImageUrl", "");
        model = prefs.getInt("biciModel", 0);

        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.bicicletas_marcas, android.R.layout.simple_spinner_item);
        brandAdapter.setDropDownViewResource(R.layout.spinner_list);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.tipo_bibicleta, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_list);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.color_bicicletas, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(R.layout.spinner_list);
        colorSpinner.setAdapter(colorAdapter);

        brandSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        typeSpinner.setOnItemSelectedListener(this);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Editar Bici</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.bike_btn_profile);
        imageView.setColorFilter(Color.parseColor("#00239E"));
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100,100, Gravity.START | Gravity.CENTER_VERTICAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        if (!imageUrl.equals("")) {
            byte[] imageBytes;
            imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profilePicture.setImageBitmap(decodedImage);
        }

        modelEditText.setText(String.valueOf(model));

        int brandPosition = brandAdapter.getPosition(brand);
        brandSpinner.setSelection(brandPosition);

        int colorPosition = colorAdapter.getPosition(color);
        colorSpinner.setSelection(colorPosition);

        int typePosition = typeAdapter.getPosition(type);
        typeSpinner.setSelection(typePosition);

        save.setOnClickListener(this::checkData);

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
        if (!brand.isEmpty() && !color.isEmpty() && !type.isEmpty() && !modelEditText.getText().toString().isEmpty())
            this.updateUser(view);
    }

    private void updateUser(View view) {
        String baseUpdateUserUrl =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);
        requestQueue = Volley.newRequestQueue(view.getContext());

        model = Integer.parseInt(modelEditText.getText().toString());

        BiciRequest biciRequest;

        if (Objects.nonNull(profilePicture.getDrawable())) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageUrl = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            biciRequest = new BiciRequest(imageUrl, brand, model, color, type);
        } else
            biciRequest = new BiciRequest(brand, model, color, type);

        Gson gson = new Gson();

        String biciJson = gson.toJson(biciRequest);

        StringRequest request = new StringRequest(Request.Method.PUT, baseUpdateUserUrl,
                response -> {
                    editor.putString("biciType", type);
                    editor.putString("biciBrand", brand);
                    editor.putString("biciColor", color);
                    editor.putInt("biciModel", model);

                    editor.commit();

                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
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