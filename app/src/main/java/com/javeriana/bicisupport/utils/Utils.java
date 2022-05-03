package com.javeriana.bicisupport.utils;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javeriana.bicisupport.models.MyLocation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Utils {

    private static Gson gson;

    public static Gson getGsonParser() {
        if(null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }

    public static String loadJson(InputStream is){
        String json;
        try{
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json= new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}
