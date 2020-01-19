package com.itea.practice.fundamentals.task.components.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.FundamentalsApp;
import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.components.manager.history.CommonDurationListener;
import com.itea.practice.fundamentals.task.components.manager.history.LogReceivedListener;
import com.itea.practice.fundamentals.task.components.manager.history.PingHistoryManager;
import com.itea.practice.fundamentals.task.components.manager.status.PingStatusManager;
import com.itea.practice.fundamentals.task.components.data.receiver.InternetReceiver;
import com.itea.practice.fundamentals.task.components.ui.history.PingHistoryActivity;

@SuppressWarnings("FieldCanBeLocal")
public class PingDashboardActivity extends AppCompatActivity implements View.OnClickListener,
        PingStatusManager.Listener,
        InternetReceiver.Listener,
        CommonDurationListener,
        LogReceivedListener {

    private InternetReceiver internetReceiver;
    private PingStatusManager statusManager;
    private PingHistoryManager historyManager;
    private PingStatusManager.Status pingStatus;

    private ImageView btnTumbler;
    private View btnHistory;
    private TextView outputConnection;
    private TextView outputStatus;
    private TextView outputDurationCommon;
    private TextView outputDurationLast;

    private FundamentalsApp getApp() {
        return ((FundamentalsApp) getApplication());
    }

    private void changeIndicatorColor(int colorId) {
        btnTumbler.setColorFilter(ContextCompat.getColor(PingDashboardActivity.this, colorId));
    }

    private void updateCommonOutput(long value) {
        outputDurationCommon.setText(getString(R.string.duration_units, value));
    }

    private void updateLastOutput(long value) {
        outputDurationLast.setText(getString(R.string.duration_units, value));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_dashboard);

        this.statusManager = getApp().getPingStateManager();
        this.historyManager = getApp().getPingHistoryManager();
        this.internetReceiver = getApp().getInternetReceiver();

        this.outputConnection = findViewById(R.id.output_connection);
        this.outputStatus = findViewById(R.id.output_status);
        this.outputDurationCommon = findViewById(R.id.output_duration_common);
        this.outputDurationLast = findViewById(R.id.output_duration_last);

        this.btnTumbler = findViewById(R.id.btn_tumbler);
        this.btnTumbler.setOnClickListener(this);

        this.btnHistory = findViewById(R.id.btn_history);
        this.btnHistory.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        try {
            updateCommonOutput(historyManager.getCommonDuration());
        } catch (NullPointerException ignored) {
            outputDurationCommon.setText("_");
        }

        try {
            updateLastOutput(historyManager.getLastSuccessfulDuration());
        } catch (NullPointerException ignored) {
            outputDurationLast.setText("_");
        }

        statusManager.addStatusListener(this);
        internetReceiver.addListener(this);
        historyManager.addCommonDelayListener(this);
        historyManager.addLogReceivedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        statusManager.removeStatusListener(this);
        internetReceiver.removeListener(this);
        historyManager.removeCommonDelayListener(this);
        historyManager.removeLogReceivedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tumbler:
                if (pingStatus == PingStatusManager.Status.NONE) {
                    statusManager.onStartCommand();
                } else {
                    statusManager.onStopCommand();
                }
                break;
            case R.id.btn_history:
                startActivity(new Intent(this, PingHistoryActivity.class));
                break;
        }
    }

    @Override
    public void onPingStatusChanged(PingStatusManager.Status value) {
        pingStatus = value;

        switch (value) {
            case NONE:
                outputStatus.setText(getString(R.string.status_inactive));
                changeIndicatorColor(R.color.highlight_inactive);
                break;
            case STARTED:
                outputStatus.setText(getString(R.string.status_inactive));
                changeIndicatorColor(R.color.accent);
                break;
            case ACTIVE:
                outputStatus.setText(getString(R.string.status_active));
                changeIndicatorColor(R.color.accent);
                break;
        }
    }

    @Override
    public void onInternetChanged(InternetReceiver.Type type) {
        switch (type) {
            case NONE:
                outputConnection.setText(R.string.connection_none);
                break;
            case MOBILE:
                outputConnection.setText(R.string.connection_mobile);
                break;
            case WIFI:
                outputConnection.setText(R.string.connection_wifi);
                break;
        }
    }

    @Override
    public void onCommonDurationChanged(long value) {
        outputDurationCommon.setText(getString(R.string.duration_units, value));
    }

    @Override
    public void onPingReceived(PingLog value) {
        outputDurationLast.setText(getString(R.string.duration_units, value.getDuration()));
    }

}
