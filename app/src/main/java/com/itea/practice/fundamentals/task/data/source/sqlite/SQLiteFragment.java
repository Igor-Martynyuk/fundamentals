package com.itea.practice.fundamentals.task.data.source.sqlite;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.task.data.source.CommonSourceFragment;

public class SQLiteFragment extends CommonSourceFragment implements SQLiteHumansAdapter.DeleteBtnListener {
    private SQLiteHumansHelper helper;
    private SQLiteHumansAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new SQLiteHumansHelper(requireContext());
        adapter = new SQLiteHumansAdapter(requireContext(), null, 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.changeCursor(helper.loadAll());
        adapter.addDeleteBtnListener(this);

        output.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        adapter.removeDeleteBtnListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        helper.close();
    }

    @Override
    protected void onClearBtnPressed() {
        if (helper.dropHumansTable()) {
            adapter.changeCursor(null);
        } else {
            Toast.makeText(requireContext(), "Clear raw failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onInputFinished() {
        if (helper.putName(input.getText().toString())) {
            adapter.changeCursor(helper.loadAll());
        } else {
            Toast.makeText(requireContext(), "Insert raw failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteBtnListener(int id) {
        if (helper.removeHumanById(id)) {
            adapter.changeCursor(helper.loadAll());
        } else {
            Toast.makeText(requireContext(), "Delete raw failed", Toast.LENGTH_SHORT).show();
        }
    }
}
