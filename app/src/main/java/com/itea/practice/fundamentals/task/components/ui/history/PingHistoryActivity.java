package com.itea.practice.fundamentals.task.components.ui.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.FundamentalsApp;
import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.components.manager.history.LogReceivedListener;
import com.itea.practice.fundamentals.task.components.manager.history.PingHistoryManager;

public class PingHistoryActivity extends AppCompatActivity implements LogReceivedListener {
    private PingHistoryManager pingHistoryManager;
    private PingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_history);

        pingHistoryManager = ((FundamentalsApp) getApplication()).getPingHistoryManager();
        adapter = new PingAdapter(this);

        RecyclerView list = findViewById(R.id.output_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.reset(pingHistoryManager.getLogs());
        pingHistoryManager.addLogReceivedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        pingHistoryManager.removeLogReceivedListener(this);
    }

    @Override
    public void onPingReceived(PingLog value) {
        adapter.insert(value);
    }

}
