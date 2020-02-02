package com.itea.practice.fundamentals.task.data.source.asssets;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HandlerThreadAssetsFragment extends AssetsFragment {
    private AssetManager manager;
    private ImagesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = requireContext().getAssets();
        adapter = new ImagesAdapter(requireContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        grid.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        LoaderHandlerThread loader = new LoaderHandlerThread("loader");
        loader.start();
        loader.prepare();

        try {
            final String[] fileNames = manager.list(imgDitName);
            assert fileNames != null;

            for (final String name : fileNames) {
                loader.postTask(new LoadImageMessage(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoaderHandlerThread extends HandlerThread {
        private Handler loadingThreadHandler;

        LoaderHandlerThread(String name) {
            super(name);
        }

        void postTask(Runnable task) {
            loadingThreadHandler.post(task);
        }

        void prepare() {
            loadingThreadHandler = new Handler(getLooper());
        }
    }

    private class LoadImageMessage implements Runnable {
        private String imgName;

        LoadImageMessage(String imgName) {
            this.imgName = imgName;
        }

        @Override
        public void run() {
            try {
                InputStream stream = manager.open(imgDitName + File.separator + imgName);
                final Bitmap image = BitmapFactory.decodeStream(stream);
                mainThreadHandel.post(new AddImageMessage(image));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddImageMessage implements Runnable {
        private Bitmap image;

        AddImageMessage(Bitmap image) {
            this.image = image;
        }

        @Override
        public void run() {
            adapter.addImage(image);
        }
    }

}
