package com.itea.practice.fundamentals.task.components.ui.history;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itea.practice.fundamentals.R;

class PingItemHolder extends RecyclerView.ViewHolder {
    private TextView outputIndex;
    private TextView outputDate;
    private TextView outputDuration;

    PingItemHolder(@NonNull View itemView) {
        super(itemView);

        outputIndex = itemView.findViewById(R.id.output_index);
        outputDate = itemView.findViewById(R.id.output_date);
        outputDuration = itemView.findViewById(R.id.output_duration);
    }

    void setIndex(String value) {
        outputIndex.setText(value);
    }

    void setResult(boolean result) {
        int colorId = result
                ? android.R.color.holo_green_dark
                : android.R.color.holo_red_dark;

        outputDate.setTextColor(ContextCompat.getColor(itemView.getContext(), colorId));
    }

    void setDate(String date) {
        outputDate.setText(date);
    }

    void setDuration(String duration) {
        outputDuration.setText(duration);
    }

}
