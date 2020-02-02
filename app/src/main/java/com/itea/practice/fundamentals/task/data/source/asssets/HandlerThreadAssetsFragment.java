package com.itea.practice.fundamentals.task.data.source.asssets;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;

import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HandlerThreadAssetsFragment extends AssetsFragment {

    @Override
    public void onResume() {
        super.onResume();

        try {
            final ImagesAdapter adapter = new ImagesAdapter(requireContext());

            final AssetManager manager = requireContext().getAssets();
            final String[] fileNames = manager.list(imgDitName);
            assert fileNames != null;

            LoaderHandlerThread thread = new LoaderHandlerThread("loader");
            thread.start();
            thread.prepare();

            for (final String name : fileNames) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream stream = manager.open(imgDitName + File.separator + name);
                            final Bitmap image = BitmapFactory.decodeStream(stream);

                            mainThreadHandel.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.addImage(image);
                                        }
                                    }
                            );

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.postTask(runnable);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LoaderHandlerThread extends HandlerThread {
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

}
