package com.itea.practice.fundamentals.task.data.source.asssets;

import android.annotation.SuppressLint;
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
        new Thread(new LoadImagesAction()).start();
    }

    private class LoadImagesAction implements Runnable {
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

                    int percentage = Math.round((float) result.size() / (float) fileNames.length * 100);

                    mainThreadHandel.post(new UpdatePercentageMessage(percentage));
                }

                mainThreadHandel.post(new SetupListMessage(result));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdatePercentageMessage implements Runnable {
        private int value;

        UpdatePercentageMessage(int value) {
            this.value = value;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            outputProgressPercentage.setText("Loading " + value + "%");
        }
    }

    private class SetupListMessage implements Runnable {
        private List<Bitmap> data;

        public SetupListMessage(List<Bitmap> data) {
            this.data = data;
        }

        @Override
        public void run() {
            grid.setAdapter(new ImagesAdapter(requireContext(), data));
            progress.setVisibility(View.GONE);
        }
    }

}
