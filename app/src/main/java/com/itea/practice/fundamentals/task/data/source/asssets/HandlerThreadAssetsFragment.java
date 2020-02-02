package com.itea.practice.fundamentals.task.data.source.asssets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HandlerThreadAssetsFragment extends AssetsFragment implements View.OnClickListener {
    private ImagesAdapter adapter;
    private LoaderHandlerThread loadingThread;
    private View loadBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ImagesAdapter(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_handler_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list.setAdapter(adapter);

        loadBtn = view.findViewById(R.id.btn_load);
        loadBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadingThread = new LoaderHandlerThread("loadingThread");
        loadingThread.start();
        loadingThread.prepare();
    }

    @Override
    public void onPause() {
        super.onPause();

        loadingThread.getLooper().quit();
        loadingThread.interrupt();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(loadBtn)) {
            loadBtn.setEnabled(false);

            String name = imgNames.get(adapter.getItemCount());
            loadingThread.load(name);
        }
    }

    private class LoaderHandlerThread extends HandlerThread {
        private Handler handler;

        LoaderHandlerThread(String name) {
            super(name);
        }

        void load(String name) {
            if (!handler.hasMessages(0)) {
                handler.post(new LoadImageTask(name));
            }
        }

        void prepare() {
            handler = new Handler(getLooper());
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
        }
    }

    private class LoadImageTask implements Runnable {
        private final String imgName;

        LoadImageTask(String imgName) {
            this.imgName = imgName;
        }

        @Override
        public void run() {
            try {

                Thread.sleep(1000);

                InputStream stream = manager.open(imgDitName + File.separator + imgName);
                final Bitmap image = BitmapFactory.decodeStream(stream);
                mainThreadHandler.post(new UpdateUITask(image));

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateUITask implements Runnable {
        private Bitmap image;

        UpdateUITask(Bitmap image) {
            this.image = image;
        }

        @Override
        public void run() {
            adapter.addImage(image);
            list.scrollToPosition(adapter.getItemCount() - 1);
            loadBtn.setEnabled(adapter.getItemCount() != imgNames.size());
        }
    }

}
