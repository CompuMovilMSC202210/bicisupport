package com.javeriana.bicisupport.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.activities.LoginActivity;
import com.javeriana.bicisupport.utils.Utils;

import org.json.JSONObject;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button logout, biciDetails, novedades;
    Button editProfile;
    TextView infoTextView;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    RequestQueue requestQueue;
    String biciType, biciBrand, biciColor;
    Integer biciModel;


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Cuenta</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        biciDetails = root.findViewById(R.id.bici_details);
        infoTextView = root.findViewById(R.id.infoTextEdit);

        prefs = getActivity().getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = prefs.edit();

        biciDetails.setOnClickListener(view -> {
            DetailBikeFragment detailBikeFragment = new DetailBikeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, detailBikeFragment);
            fragmentTransaction.commit();
        });

        logout = root.findViewById(R.id.logout);
        biciDetails = root.findViewById(R.id.bici_details);
        novedades = root.findViewById(R.id.novedades);
        editProfile = root.findViewById(R.id.edit_profile);

        getUserData(root);

        logout.setOnClickListener(view -> {
            editor.clear();
            editor.commit();

            startActivity(new Intent(view.getContext(), LoginActivity.class));
        });
        biciDetails.setOnClickListener(view -> {
            DetailBikeFragment fragment = new DetailBikeFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
        });
        novedades.setOnClickListener(view -> {
            IncidentsListFragment fragment = new IncidentsListFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
        });

        editProfile.setOnClickListener(view -> {
            ModifyProfileFragment modifyProfileFragment = new ModifyProfileFragment();
            FragmentTransaction fragmentTransaction = ((HomeActivity) getContext()).getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragmentContainerView, modifyProfileFragment);
            fragmentTransaction.commit();
        });


        return root;
    }

    private void getUserData(View view) {
        requestQueue = Volley.newRequestQueue(view.getContext());
        String localId = prefs.getString("localId", "");
        String url =
                String.format("https://bici-support-api.herokuapp.com/api/v1/users/%s", localId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    JSONObject bici = Utils.getJsonObjectValueFromJsonObjectByName(response, "bici");

                    String name = Utils.getStringValueFromJsonObjectByName(response, "name");
                    String user = Utils.getStringValueFromJsonObjectByName(response, "user");

                    biciType = Utils.getStringValueFromJsonObjectByName(bici, "type");
                    biciBrand = Utils.getStringValueFromJsonObjectByName(bici, "brand");
                    biciColor = Utils.getStringValueFromJsonObjectByName(bici, "color");
                    biciModel = Utils.getIntValueFromJsonObjectByName(bici, "model");

                    editor.putString("userName", name);
                    editor.putString("userUser", user);

                    editor.putString("biciType", biciType);
                    editor.putString("biciBrand", biciBrand);
                    editor.putString("biciColor", biciColor);
                    editor.putInt("biciModel", biciModel);

                    editor.commit();

                    String info = String.format("%s\n\n%s", name, user);
                    infoTextView.setText(info);
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
                            .setMessage("No es posible recuperar el usuario")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error recuperando el usuario");
                    alert.show();
                });

        requestQueue.add(request);
    }
}