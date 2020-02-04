package com.itea.practice.fundamentals.task.data.source.web;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itea.practice.fundamentals.R;

@SuppressWarnings("FieldCanBeLocal")
public class WebImageFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Bitmap> {
    private final int LOADER_ID = 1;

    private ImageView outputImg;
    private View btnSave;
    private View btnLoad;

    private void load() {
        btnSave.setEnabled(false);
        btnLoad.setEnabled(false);

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this).startLoading();
    }

    private void save() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_img, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        outputImg = view.findViewById(R.id.output_img);
        btnSave = view.findViewById(R.id.btn_save);
        btnLoad = view.findViewById(R.id.btn_load);
    }

    @Override
    public void onStart() {
        super.onStart();

        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View clicked) {
        if (clicked == btnSave) save();
        if (clicked == btnLoad) load();
    }

    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
        return new WebImageLoader(requireContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
        btnSave.setEnabled(true);
        btnLoad.setEnabled(true);

        outputImg.setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {/*nothing*/}
}
