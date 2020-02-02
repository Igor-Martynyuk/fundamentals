package com.itea.practice.fundamentals.task.data.source.asssets.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetImagesLoader extends AsyncTaskLoader<List<Bitmap>> {
    private AssetManager assetManager;
    private String dirName;
    private List<String> fileNames;

    AssetImagesLoader(@NonNull Context context, AssetManager assetManager, String dirName, List<String> fileNames) {
        super(context);

        this.assetManager = assetManager;
        this.dirName = dirName;
        this.fileNames = fileNames;
    }

    private void delay() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImage(String path) {
        try {
            return BitmapFactory.decodeStream(assetManager.open(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Nullable
    @Override
    public List<Bitmap> loadInBackground() {
        delay();

        final List<Bitmap> result = new ArrayList<>();

        for (String name : fileNames) {
            Bitmap img = loadImage(dirName + File.separator + name);
            result.add(img);
        }

        return result;
    }

}
