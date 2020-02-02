package com.itea.practice.fundamentals.task.data.source.asssets.loader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;
import com.itea.practice.fundamentals.task.data.source.asssets.common.ImagesAdapter;

import java.util.List;

public class LoaderAssetsFragment extends AssetsFragment implements LoaderManager.LoaderCallbacks<List<Bitmap>> {
    private final static int LOADER_ID = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_thread, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this).startLoading();
    }

    @NonNull
    @Override
    public Loader<List<Bitmap>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AssetImagesLoader(requireContext(), assetManager, imgDitName, imgNames);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Bitmap>> loader, List<Bitmap> data) {
        progress.setVisibility(View.GONE);
        list.setAdapter(new ImagesAdapter(requireContext(), data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Bitmap>> loader) {/*nothing*/}

}
