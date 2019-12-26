package com.itea.practice.fundamentals.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.itea.practice.fundamentals.R;

public class PingHistoryActivity extends AppCompatActivity {
    private PingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_history);

        RecyclerView list = findViewById(R.id.output_list);
        list.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PingAdapter(this);
        list.setAdapter(adapter);
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = getContentResolver().query(
                PingHistoryProvider.HISTORY_URI,
                new String[]{
                        PingHistoryProvider.FIELD_DATE,
                        PingHistoryProvider.FIELD_RESULT,
                        PingHistoryProvider.FIELD_DURATION
                },
                null,
                null,
                null
        );

        adapter.setCursor(cursor);
        adapter.notifyDataSetChanged();

    }

}
