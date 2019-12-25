package com.itea.practice.fundamentals.task;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
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
import com.itea.practice.fundamentals.R;

@SuppressWarnings("FieldCanBeLocal")
public class PingDashboardActivity extends AppCompatActivity implements InternetReceiver.Listener, View.OnClickListener {
    private final String KEY_STATE_CONNECTION = "state_connection";
    private final String KEY_STATE_STATUS = "state_status";
    private final String KEY_STATE_DELAY = "state_delay";

    private String currentConnectionType = null;

    private InternetReceiver receiver;

    private ImageView btnTumbler;
    private View btnHistory;
    private TextView outputConnection;
    private TextView outputStatus;
    private TextView outputDelay;

    @SuppressLint("SetTextI18n")
    public void updateDelay() {

        Cursor cursor = getContentResolver().query(
                PingHistoryProvider.HISTORY_URI,
                new String[]{PingHistoryProvider.FIELD_DURATION},
                PingHistoryProvider.SELECTION_FILTERED,
                null,
                null
        );

        long delaySum = 0L;
        long commonDelay = 0L;
        long lastDelay = 0L;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                long delay = cursor.getLong(0);

                delaySum += delay;
                lastDelay = delay;

                cursor.moveToNext();
            }
            commonDelay = delaySum / cursor.getCount();

            cursor.close();
        }

        outputDelay.setText(
                lastDelay
                        + "ms "
                        + getString(R.string.last).toLowerCase()
                        + "\n"
                        + commonDelay
                        + "ms "
                        + getString(R.string.common).toLowerCase()
        );
    }

    private PingServiceBinder.PingListener pingListener = new PingServiceBinder.PingListener() {
        @Override
        public void onPing(final PingLog log) {
            getContentResolver().insert(
                    PingHistoryProvider.HISTORY_URI,
                    PingHistoryProvider.logToValues(log)
            );

            updateDelay();
        }
    };

    private PingServiceBinder.ExecutionStateListener stateListener = new PingServiceBinder.ExecutionStateListener() {
        @Override
        public void onStateChanged(boolean state) {
            outputStatus.setText(getString(state ? R.string.status_active : R.string.status_inactive));
        }
    };

    private PingServiceBinder pingBinder;
    private ServiceConnection pingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            PingDashboardActivity.this.pingBinder = (PingServiceBinder) binder;
            PingDashboardActivity.this.pingBinder.setOnPingListener(pingListener);

            pingBinder.setStateListener(stateListener);

            if (currentConnectionType != null)
                pingBinder.startPingProcess();

            changeIndicatorColor(R.color.accent);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pingBinder.stopPingProcess();
            pingBinder = null;

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
        this.btnTumbler.setOnClickListener(this);

        this.btnHistory = findViewById(R.id.btn_history);
        this.btnHistory.setOnClickListener(this);
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

        updateDelay();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.receiver.removeListener(this);
        this.unregisterReceiver(this.receiver);

        if (pingBinder != null) {
            this.unbindService(pingConnection);
        }
    }

    @Override
    public void onInternetChanged(@Nullable String type) {
        if (currentConnectionType == null && type == null) return;

        if (currentConnectionType == null) {

            currentConnectionType = type;
            outputConnection.setText(type);

            if (pingBinder != null && !pingBinder.isActive()) {
                pingBinder.startPingProcess();
            }

        } else if (type == null) {
            currentConnectionType = null;
            outputConnection.setText(getString(R.string.connection_none));

            if (pingBinder != null && pingBinder.isActive()) {
                pingBinder.stopPingProcess();
            }

        } else if (!currentConnectionType.equals(type)) {

            currentConnectionType = type;
            outputConnection.setText(type);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tumbler:

                if (this.pingBinder == null) {

                    this.startService(new Intent(this, PingService.class));
                    this.bindService(
                            new Intent(this, PingService.class),
                            this.pingConnection,
                            0
                    );

                } else {

                    this.pingBinder.stopPingProcess();
                    this.unbindService(pingConnection);
                    this.pingBinder = null;
                    this.changeIndicatorColor(R.color.highlight_inactive);

                    this.stopService(new Intent(PingDashboardActivity.this, PingService.class));

                }

                break;
            case R.id.btn_history:
                startActivity(new Intent(this, PingHistoryActivity.class));
                break;
        }
    }
}
