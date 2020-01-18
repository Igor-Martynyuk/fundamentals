package com.itea.practice.fundamentals.task.components.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.components.data.PingHistoryProvider;

public class PingAdapter extends RecyclerView.Adapter<PingItemHolder> {
    private Context context;
    private Cursor cursor;

    PingAdapter(Context context) {
        this.context = context;
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public PingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PingItemHolder(LayoutInflater.from(context).inflate(R.layout.ping_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PingItemHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.setDate(String.valueOf(cursor.getLong(0)));
        holder.setResult(cursor.getInt(1) == PingHistoryProvider.RESULT_SUCCESS);
        holder.setDuration(cursor.getLong(2) + "ms");
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
