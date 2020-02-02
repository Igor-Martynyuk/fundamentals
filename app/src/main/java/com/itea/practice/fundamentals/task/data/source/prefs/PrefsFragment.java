package com.itea.practice.fundamentals.task.data.source.prefs;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.itea.practice.fundamentals.R;

public class PrefsFragment extends Fragment {

    private final String KEY_HUMANS = "humans";

    private SharedPreferences preferences;
    private HumansListener humansListener;

    private EditText input;
    private InputFinishedListener inputFinishedListener;

    private ListView output;
    private HumanAdapter adapter;

    private View btnClear;
    private ClearBtnListener clearBtnListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        humansListener = new HumansListener();

        adapter = new HumanAdapter();

        inputFinishedListener = new InputFinishedListener();
        clearBtnListener = new ClearBtnListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prefs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = requireView().findViewById(R.id.input_human);
        input.setOnEditorActionListener(inputFinishedListener);

        output = requireView().findViewById(R.id.output_humans);
        output.setAdapter(adapter);

        btnClear = requireView().findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(clearBtnListener);

    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
        preferences.registerOnSharedPreferenceChangeListener(humansListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        preferences.unregisterOnSharedPreferenceChangeListener(humansListener);
    }

    private class HumansListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            adapter.notifyDataSetChanged();
        }
    }

    private class InputFinishedListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            preferences.edit().putString(
                    KEY_HUMANS + preferences.getAll().size(),
                    view.getText().toString()
            ).apply();

            view.setText("");
            view.clearFocus();

            return false;
        }
    }

    private class ClearBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View ignored) {
            preferences.edit().clear().apply();
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
