package com.itea.practice.fundamentals.task.data.source.asssets;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.View;

import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ThreadAssetsFragment extends AssetsFragment {

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final List<Bitmap> result = new ArrayList<>();

                            AssetManager manager = requireContext().getAssets();
                            final String[] fileNames = manager.list(imgDitName);
                            assert fileNames != null;

                            for (String fileName : fileNames) {
                                Thread.sleep(300);

                                InputStream stream = manager.open(imgDitName + File.separator + fileName);
                                Bitmap image = BitmapFactory.decodeStream(stream);
                                result.add(image);

                                mainThreadHandel.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                float percentage = (float) result.size() / (float) fileNames.length * 100;
                                                outputProgressPercentage.setText("Loading " + (int) percentage + "%");
                                            }
                                        }
                                );
                            }

                            mainThreadHandel.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            grid.setAdapter(new ImagesAdapter(requireContext(), result));
                                            progress.setVisibility(View.GONE);
                                        }
                                    }
                            );
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        thread.start();
    }

}
