package com.itea.practice.fundamentals.task.components.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PingAdapter extends RecyclerView.Adapter<PingItemHolder> {
    private Context context;
    private List<PingLog> logs;
    private DateFormat dateFormat;

    PingAdapter(Context context) {
        this.context = context;
        this.logs = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.getDefault());
    }

    void reset(List<PingLog> logs) {
        this.logs.clear();
        this.logs.addAll(logs);

        notifyDataSetChanged();
    }

    void insert(PingLog value) {
        logs.add(value);

        notifyItemChanged(logs.size() - 1);
    }

    @NonNull
    @Override
    public PingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PingItemHolder(LayoutInflater.from(context).inflate(R.layout.ping_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PingItemHolder holder, int position) {
        PingLog current = logs.get(position);

        holder.setIndex(context.getString(R.string.index, holder.getAdapterPosition()));
        holder.setDate(dateFormat.format(current.getDate()));
        holder.setResult(current.getResult());
        holder.setDuration(context.getString(R.string.duration_units, current.getDuration()));
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

}
