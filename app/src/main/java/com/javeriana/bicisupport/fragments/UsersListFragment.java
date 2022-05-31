package com.javeriana.bicisupport.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javeriana.bicisupport.R;
import com.javeriana.bicisupport.activities.HomeActivity;
import com.javeriana.bicisupport.models.UserList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersListFragment extends Fragment {

    ListView usersListView;
    RequestQueue requestQueue;

    List<UserList> usersList;

    public UsersListFragment() {
        // Required empty public constructor
    }

    public static UsersListFragment newInstance(String param1, String param2) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users_list, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setTitle(HtmlCompat.fromHtml("<font color='#00239E'>Lista de usuarios</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        usersList = new ArrayList<>();
        usersListView = root.findViewById(R.id.users_list_view);

        getAllUsers(root);

        usersListView.setOnItemClickListener((parent, view, position, id) -> {
            ChatFragment chatFragment = new ChatFragment();
            FragmentTransaction fragmentTransaction = ((HomeActivity) getContext()).getSupportFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("cloudToken", usersList.get(position).getCloudToken());
            bundle.putString("destinyName", usersList.get(position).getName());
            chatFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.fragmentContainerView, chatFragment);
            fragmentTransaction.commit();
        });

        return root;
    }

    private void getAllUsers(View view) {
        String baseGetAllUrl = "https://bici-support-api.herokuapp.com/api/v1/users";
        requestQueue = Volley.newRequestQueue(view.getContext());

        Type listType = new TypeToken<ArrayList<UserList>>() {
        }.getType();

        StringRequest request = new StringRequest(Request.Method.GET, baseGetAllUrl,
                response -> {
                    usersList = new Gson().fromJson(response, listType);
                    ArrayAdapter<UserList> adapter = new ArrayAdapter<>(view.getContext(), R.layout.custom_list_names, usersList);
                    usersListView.setAdapter(adapter);
                },
                error -> {
                    AlertDialog alert = new AlertDialog.Builder(view.getContext())
                            .setMessage("Ha ocurrido un error obteniendo los datos")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                            .create();
                    alert.setTitle("Error en el registro");
                    alert.show();
                });

        requestQueue.add(request);
    }
}