package com.itea.practice.fundamentals.task.data.source.commons.prefs;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.data.source.commons.CommonSourceFragment;

public class PrefsFragment extends CommonSourceFragment {

    private SharedPreferences preferences;
    private HumanAdapter adapter;

    private HumansListener humansListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        adapter = new HumanAdapter();
        humansListener = new HumansListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        output.setAdapter(adapter);
        preferences.registerOnSharedPreferenceChangeListener(humansListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        preferences.unregisterOnSharedPreferenceChangeListener(humansListener);
    }

    @Override
    protected void onClearBtnPressed() {
        preferences.edit().clear().apply();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onInputFinished() {
        preferences.edit().putString("humans" + preferences.getAll().size(), input.getText().toString()).apply();

        input.setText("");
        input.clearFocus();
    }

    private class HumansListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            adapter.notifyDataSetChanged();
        }
    }

    private class HumanAdapter extends BaseAdapter {
        private String getKey(int position) {
            return (String) preferences.getAll().keySet().toArray()[position];
        }

        @Override
        public int getCount() {
            return preferences.getAll().size();
        }

        @Override
        public String getItem(int position) {
            return preferences.getString(getKey(position), null);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.item_human,
                        parent,
                        false
                );
            }

            TextView outputName = convertView.findViewById(R.id.output_name);
            outputName.setText(getItem(position));

            View btnRemove = convertView.findViewById(R.id.btn_remove);
            btnRemove.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            preferences.edit().remove(getKey(position)).apply();
                        }
                    }
            );

            return convertView;
        }
    }

}
