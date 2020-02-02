package com.itea.practice.fundamentals.task.data.source.asssets.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itea.practice.fundamentals.R;

public abstract class AssetsFragment extends Fragment {
    protected String imgDitName = "img";
    protected View progress;
    protected TextView outputProgressPercentage;

    protected RecyclerView grid;
    protected final Handler mainThreadHandel = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = view.findViewById(R.id.group_progress);
        outputProgressPercentage = view.findViewById(R.id.output_progress_percentage);
        grid = view.findViewById(R.id.grid);
    }

    @Override
    public void onResume() {
        super.onResume();

        grid.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
