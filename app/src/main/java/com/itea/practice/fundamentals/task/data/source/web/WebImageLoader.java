package com.itea.practice.fundamentals.task.data.source.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("FieldCanBeLocal")
public class WebImageLoader extends AsyncTaskLoader<Bitmap> {
    private final String METHOD_GET = "GET";
    private final String KEY_IMG_URL = "message";

    private JsonParser parser;

    WebImageLoader(@NonNull Context context) {
        super(context);

        parser = new JsonParser();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Nullable
    @Override
    public Bitmap loadInBackground() {
        HttpsURLConnection jsonConnection = null;
        HttpsURLConnection imgConnection = null;

        try {

            URL jsonRequest = new URL("https://dog.ceo/api/breeds/image/random");
            jsonConnection = (HttpsURLConnection) jsonRequest.openConnection();
            jsonConnection.setRequestMethod(METHOD_GET);
            jsonConnection.setChunkedStreamingMode(0);

            InputStream inputResponse = jsonConnection.getInputStream();
            String strResponse = new Scanner(inputResponse, StandardCharsets.UTF_8.name()).next();
            JsonElement json = parser.parse(strResponse);
            String address = json.getAsJsonObject().get(KEY_IMG_URL).getAsString();

            URL imgRequest = new URL(address);
            imgConnection = (HttpsURLConnection) imgRequest.openConnection();
            imgConnection.setRequestMethod(METHOD_GET);
            InputStream imgResponse = imgConnection.getInputStream();
            Bitmap img = BitmapFactory.decodeStream(imgResponse);

            return img;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jsonConnection != null) jsonConnection.disconnect();
            if (imgConnection != null) imgConnection.disconnect();
        }
    }

}
