package com.itea.practice.fundamentals.task.data.source.asssets.common;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.itea.practice.fundamentals.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AssetsFragment extends Fragment {
    protected String imgDitName = "img";
    protected List<String> imgNames;
    protected AssetManager assetManager;

    protected View progress;
    protected TextView outputProgressPercentage;

    protected RecyclerView list;
    protected final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    protected void updateProgressPercentage(int value) {
        outputProgressPercentage.setText("Loading " + value + "%");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetManager = requireContext().getAssets();
        imgNames = new ArrayList<>();

        try {
            //noinspection ConstantConditions
            imgNames.addAll(Arrays.asList(assetManager.list(imgDitName)));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = view.findViewById(R.id.group_progress);
        outputProgressPercentage = view.findViewById(R.id.output_progress_percentage);
        list = view.findViewById(R.id.output_images);
    }

    @Override
    public void onResume() {
        super.onResume();

        list.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
