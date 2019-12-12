package com.itea.practice.fundamentals.task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.itea.practice.components.PingLog;
import com.itea.practice.components.R;

public class PingDashboardActivity extends AppCompatActivity implements InternetReceiver.Listener {
    private final String KEY_STATE_CONNECTION = "state_connection";
    private final String KEY_STATE_STATUS = "state_status";
    private final String KEY_STATE_DELAY = "state_delay";

    private InternetReceiver receiver;

    private ImageView btnTumbler;
    private TextView outputConnection;
    private TextView outputStatus;
    private TextView outputDelay;

    private PingServiceBinder.PingListener pingListener = new PingServiceBinder.PingListener() {
        @Override
        public void onPing(PingLog log) {
            outputDelay.setText(String.valueOf(log.getDuration()));
        }
    };

    private PingServiceBinder pingBinder;
    private ServiceConnection pingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            PingDashboardActivity.this.pingBinder = (PingServiceBinder) binder;
            PingDashboardActivity.this.pingBinder.setOnPingListener(pingListener);

            changeIndicatorColor(R.color.accent);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            PingDashboardActivity.this.pingBinder = null;
            changeIndicatorColor(R.color.highlight_inactive);
        }
    };

    private void changeIndicatorColor(int colorId) {
        PingDashboardActivity.this.btnTumbler.setColorFilter(
                ContextCompat.getColor(
                        PingDashboardActivity.this,
                        colorId
                )
        );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        state.putString(KEY_STATE_CONNECTION, outputConnection.getText().toString());
        state.putString(KEY_STATE_STATUS, outputStatus.getText().toString());
        state.putString(KEY_STATE_DELAY, outputDelay.getText().toString());

        super.onSaveInstanceState(state);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_dashboard);

        this.outputConnection = findViewById(R.id.output_connection);
        this.outputStatus = findViewById(R.id.output_status);
        this.outputDelay = findViewById(R.id.output_delay);

        this.btnTumbler = findViewById(R.id.btn_tumbler);
        this.btnTumbler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PingDashboardActivity.this.pingBinder == null) {
                            PingDashboardActivity.this.startService(new Intent(PingDashboardActivity.this, PingService.class));
                            PingDashboardActivity.this.bindService(
                                    new Intent(PingDashboardActivity.this, PingService.class),
                                    PingDashboardActivity.this.pingConnection,
                                    0
                            );
                        } else {
                            PingDashboardActivity.this.unbindService(pingConnection);
                            PingDashboardActivity.this.pingBinder = null;
                            PingDashboardActivity.this.changeIndicatorColor(R.color.highlight_inactive);

                            PingDashboardActivity.this.stopService(new Intent(PingDashboardActivity.this, PingService.class));
                        }
                    }
                }
        );
    }

    @Override
    protected void onPostCreate(@Nullable Bundle state) {
        super.onPostCreate(state);

        if (state != null) {
            this.outputConnection.setText(
                    state.getString(
                            KEY_STATE_CONNECTION,
                            getString(R.string.connection_none)
                    )
            );

            this.outputConnection.setText(
                    state.getString(
                            KEY_STATE_STATUS,
                            getString(R.string.status_inactive)
                    )
            );

            this.outputConnection.setText(
                    state.getString(
                            KEY_STATE_DELAY,
                            "0"
                    )
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.receiver = new InternetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        this.registerReceiver(receiver, filter);
        this.receiver.addListener(this);

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        assert manager != null;
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (info.service.getClassName().equals(PingService.class.getName())) {
                PingDashboardActivity.this.bindService(
                        new Intent(PingDashboardActivity.this, PingService.class),
                        pingConnection,
                        0
                );
                break;
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        this.receiver.removeListener(this);
        this.unregisterReceiver(this.receiver);

        this.unbindService(pingConnection);
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
