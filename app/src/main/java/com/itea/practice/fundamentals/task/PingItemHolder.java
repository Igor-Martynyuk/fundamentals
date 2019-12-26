package com.itea.practice.fundamentals.task;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.fundamentals.R;

public class PingItemHolder extends RecyclerView.ViewHolder {
    private TextView outputDate;
    private TextView outputDuration;

    public PingItemHolder(@NonNull View itemView) {
        super(itemView);

        outputDate = itemView.findViewById(R.id.output_date);
        outputDuration = itemView.findViewById(R.id.output_duration);
    }

    public void setResult(boolean result) {
        int colorId = result
                ? android.R.color.holo_green_dark
                : android.R.color.holo_red_dark;

        outputDate.setTextColor(ContextCompat.getColor(itemView.getContext(), colorId));
    }

    public void setDate(String date) {
        outputDate.setText(date);
    }

    public void setDuration(String duration) {
        outputDuration.setText(duration);
    }

}
