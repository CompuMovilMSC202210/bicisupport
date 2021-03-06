package com.javeriana.bicisupport.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {

    private static Gson gson;

    public static Gson getGsonParser() {
        if (null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }

    public static String loadJson(InputStream is) {
        String json;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getStringValueFromJsonObjectByName(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            return "";
        }
    }

    public static Integer getIntValueFromJsonObjectByName(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getInt(name);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject getJsonObjectValueFromJsonObjectByName(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONObject(name);
        } catch (JSONException e) {
            return null;
        }
    }
}
