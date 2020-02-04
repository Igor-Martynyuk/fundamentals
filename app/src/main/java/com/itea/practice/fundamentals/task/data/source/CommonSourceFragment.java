package com.itea.practice.fundamentals.task.data.source;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itea.practice.fundamentals.R;

public abstract class CommonSourceFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {
    protected EditText input;
    protected ListView output;
    protected View btnClear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prefs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = requireView().findViewById(R.id.input_human);
        input.setOnEditorActionListener(this);

        output = requireView().findViewById(R.id.output_humans);

        btnClear = requireView().findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnClear)) onClearBtnPressed();
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        onInputFinished();

        input.clearFocus();
        input.setText("");

        return false;
    }

    protected abstract void onClearBtnPressed();

    protected abstract void onInputFinished();

}
