package com.itea.practice.fundamentals.task.data.source.asssets.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.asssets.common.AssetsFragment;

import java.util.List;

public class AsyncTaskAssetsFragment extends AssetsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_thread, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        AsyncTask<String, Integer, List<Bitmap>> task = new AssetLoadingAsyncTask(
                requireContext(),
                imgDitName,
                imgNames,
                assetManager,
                list,
                progress,
                outputProgressPercentage
        );

        task.execute(imgNames.toArray(new String[]{}));
    }

}
