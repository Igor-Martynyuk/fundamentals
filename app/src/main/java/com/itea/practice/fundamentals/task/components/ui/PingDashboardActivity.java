package com.itea.practice.fundamentals.task.components.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.itea.practice.fundamentals.FundamentalsApp;
import com.itea.practice.fundamentals.R;
import com.itea.practice.fundamentals.task.components.controller.PingStatusController;
import com.itea.practice.fundamentals.task.components.data.PingInternetReceiver;

@SuppressWarnings("FieldCanBeLocal")
public class PingDashboardActivity extends AppCompatActivity implements View.OnClickListener,
        PingStatusController.Listener, PingInternetReceiver.Listener {

    private PingInternetReceiver internetReceiver;
    private PingStatusController statusController;
    private PingStatusController.Status pingStatus;

    private ImageView btnTumbler;
    private View btnHistory;
    private TextView outputConnection;
    private TextView outputStatus;
    private TextView outputDelay;


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
        this.internetReceiver = getApp().getInternetReceiver();

        this.outputConnection = findViewById(R.id.output_connection);
        this.outputStatus = findViewById(R.id.output_status);
        this.outputDelay = findViewById(R.id.output_delay);

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
    }

    @Override
    protected void onPause() {
        super.onPause();

        statusController.removeStatusListener(this);
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
    public void onInternetChanged(PingInternetReceiver.Type type) {
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

}
