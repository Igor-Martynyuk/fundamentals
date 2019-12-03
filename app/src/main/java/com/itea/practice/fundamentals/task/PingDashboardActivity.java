package com.itea.practice.fundamentals.task;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itea.practice.components.R;

public class PingDashboardActivity extends AppCompatActivity implements InternetReceiver.Listener {
    private InternetReceiver receiver;

    private TextView outputConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_dashboard);

        this.outputConnection = findViewById(R.id.output_connection);

        receiver = new InternetReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.receiver.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.receiver.removeListener(this);
    }

    @Override
    public void onInternetChanged(@Nullable String type) {
        this.outputConnection.setText(
                type == null
                        ? getString(R.string.connection_none)
                        : type
        );
    }

}
