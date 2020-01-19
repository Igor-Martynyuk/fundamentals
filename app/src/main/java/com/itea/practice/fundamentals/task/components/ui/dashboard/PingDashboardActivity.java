package com.itea.practice.fundamentals.task.components.ui.dashboard;

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
import com.itea.practice.fundamentals.task.components.controller.PingHistoryController;
import com.itea.practice.fundamentals.task.components.controller.PingStatusController;
import com.itea.practice.fundamentals.task.components.data.InternetReceiver;
import com.itea.practice.fundamentals.task.components.ui.history.PingHistoryActivity;

@SuppressWarnings("FieldCanBeLocal")
public class PingDashboardActivity extends AppCompatActivity implements View.OnClickListener,
        PingStatusController.Listener,
        InternetReceiver.Listener,
        PingHistoryController.CommonDelayListener,
        PingHistoryController.LogReceivedListener {

    private InternetReceiver internetReceiver;
    private PingStatusController statusController;
    private PingHistoryController historyController;
    private PingStatusController.Status pingStatus;

    private ImageView btnTumbler;
    private View btnHistory;
    private TextView outputConnection;
    private TextView outputStatus;
    private TextView outputDelayCommon;
    private TextView outputDelayLast;

    private FundamentalsApp getApp() {
        return ((FundamentalsApp) getApplication());
    }

    private void changeIndicatorColor(int colorId) {
        btnTumbler.setColorFilter(ContextCompat.getColor(PingDashboardActivity.this, colorId));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_dashboard);

        this.statusController = getApp().getStateController();
        this.historyController = getApp().getHistoryController();
        this.internetReceiver = getApp().getInternetReceiver();

        this.outputConnection = findViewById(R.id.output_connection);
        this.outputStatus = findViewById(R.id.output_status);
        this.outputDelayCommon = findViewById(R.id.output_delay_common);
        this.outputDelayLast = findViewById(R.id.output_delay_last);

        this.btnTumbler = findViewById(R.id.btn_tumbler);
        this.btnTumbler.setOnClickListener(this);

        this.btnHistory = findViewById(R.id.btn_history);
        this.btnHistory.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        statusController.addStatusListener(this);
        internetReceiver.addListener(this);
        historyController.addCommonDelayListener(this);
        historyController.addLogReceivedListener(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        statusController.removeStatusListener(this);
        internetReceiver.removeListener(this);
        historyController.removeCommonDelayListener(this);
        historyController.removeLogReceivedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tumbler:
                if (pingStatus == PingStatusController.Status.NONE) {
                    statusController.onStartCommand();
                } else {
                    statusController.onStopCommand();
                }
                break;
            case R.id.btn_history:
                startActivity(new Intent(this, PingHistoryActivity.class));
                break;
        }
    }

    @Override
    public void onPingStatusChanged(PingStatusController.Status value) {
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
    public void onCommonDelayChanged(long value) {
        outputDelayCommon.setText(String.valueOf(value));
    }

    @Override
    public void onLogReceived(PingLog value) {
        outputDelayLast.setText(String.valueOf(value.getDuration()));
    }
}
