package com.itea.practice.fundamentals.task.data.source.commons.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.itea.practice.fundamentals.R;

import java.util.ArrayList;
import java.util.List;

class SQLiteHumansAdapter extends CursorAdapter {
    private Context context;
    private List<DeleteBtnListener> deleteBtnListeners;

    SQLiteHumansAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.context = context;
        this.deleteBtnListeners = new ArrayList<>();
    }

    public void notifyDelete(int id) {
        for (DeleteBtnListener listener : deleteBtnListeners) {
            listener.onDeleteBtnListener(id);
        }
    }

    public void addDeleteBtnListener(DeleteBtnListener listener) {
        deleteBtnListeners.add(listener);
    }

    public void removeDeleteBtnListener(DeleteBtnListener listener) {
        deleteBtnListeners.remove(listener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_human, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ((TextView) view.findViewById(R.id.output_name)).setText(cursor.getString(1));
        view.findViewById(R.id.btn_remove).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDelete(cursor.getInt(0));
                    }
                }
        );
    }

    interface DeleteBtnListener {

        void onDeleteBtnListener(int id);
    }
}
