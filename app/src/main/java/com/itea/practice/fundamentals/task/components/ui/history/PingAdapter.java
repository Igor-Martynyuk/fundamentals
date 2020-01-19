package com.itea.practice.fundamentals.task.components.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.R;

import java.util.ArrayList;
import java.util.List;

public class PingAdapter extends RecyclerView.Adapter<PingItemHolder> {
    private Context context;
    private List<PingLog> logs;

    PingAdapter(Context context) {
        this.context = context;
        this.logs = new ArrayList<>();
    }

    void reset(List<PingLog> logs){
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
        return new PingItemHolder(LayoutInflater.from(context).inflate(R.layout.ping_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PingItemHolder holder, int position) {
        PingLog current = logs.get(position);

        holder.setDate(String.valueOf(current.getDate()));
        holder.setResult(current.getResult());
        holder.setDuration(current.getDuration() + "ms");
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

}
