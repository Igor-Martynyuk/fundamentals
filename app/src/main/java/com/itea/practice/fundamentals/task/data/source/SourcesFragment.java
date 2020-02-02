package com.itea.practice.fundamentals.task.data.source;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.itea.practice.fundamentals.R;

public class SourcesFragment extends Fragment {
    private ListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new String[]{
                        "SharedPreference",
                        "Assets(Thread)",
                        "Assets(HandlerThread)",
                        "SQLite",
                        "WebAPI"
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = requireView().findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) requireActivity());
    }

}
