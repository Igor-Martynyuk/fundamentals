package com.itea.practice.fundamentals.task.data.source.asssets;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ThreadAssetsFragment extends AssetsFragment {
    private Thread loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_thread, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        loader = new Thread(new LoadImagesTask());
        loader.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        loader.interrupt();
    }

    private class LoadImagesTask implements Runnable {
        @Override
        public void run() {
            try {
                final List<Bitmap> result = new ArrayList<>();

                for (String fileName : imgNames) {
                    Thread.sleep(300);

                    InputStream stream = manager.open(imgDitName + File.separator + fileName);
                    Bitmap image = BitmapFactory.decodeStream(stream);
                    result.add(image);

                    int percentage = Math.round((float) result.size() / (float) imgNames.size() * 100);

                    mainThreadHandler.post(new UpdatePercentageTask(percentage));
                }

                mainThreadHandler.post(new SetupListTask(result));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdatePercentageTask implements Runnable {
        private int value;

        UpdatePercentageTask(int value) {
            this.value = value;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            outputProgressPercentage.setText("Loading " + value + "%");
        }
    }

    private class SetupListTask implements Runnable {
        private List<Bitmap> data;

        SetupListTask(List<Bitmap> data) {
            this.data = data;
        }

        @Override
        public void run() {
            list.setAdapter(new ImagesAdapter(requireContext(), data));
            progress.setVisibility(View.GONE);
        }
    }

}
