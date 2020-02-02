package com.itea.practice.fundamentals.task.data.source.asssets.task;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetLoadingAsyncTask extends AsyncTask<String, Integer, List<Bitmap>> {
    private Context context;

    private String imgDirName;
    private List<String> fileNames;

    private AssetManager assetManager;
    private RecyclerView list;
    private View progressView;
    private TextView outputPercentage;

    public AssetLoadingAsyncTask(Context context,
                                 String imgDirName,
                                 List<String> fileNames,
                                 AssetManager assetManager,
                                 RecyclerView list,
                                 View progressView,
                                 TextView outputPercentage) {

        this.context = context;
        this.imgDirName = imgDirName;
        this.fileNames = fileNames;
        this.assetManager = assetManager;
        this.list = list;
        this.progressView = progressView;
        this.outputPercentage = outputPercentage;
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
    protected void onPreExecute() {
        super.onPreExecute();

        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Bitmap> doInBackground(String... args) {
        delay();

        final List<Bitmap> result = new ArrayList<>();

        for (String name : args) {
            Bitmap img = loadImage(imgDirName + File.separator + name);
            result.add(img);

            int percentage = Math.round((float) result.size() / (float) fileNames.size() * 100);
            publishProgress(percentage);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Bitmap> result) {
        super.onPostExecute(result);

        progressView.setVisibility(View.GONE);
        list.setAdapter(new ImagesAdapter(context, result));
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        outputPercentage.setText(String.valueOf(values[0]));
    }
}
