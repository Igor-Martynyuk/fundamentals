package com.itea.practice.fundamentals;

import android.app.Application;
import android.content.IntentFilter;

import com.itea.practice.fundamentals.task.components.controller.PingHistoryController;
import com.itea.practice.fundamentals.task.components.controller.PingStatusController;
import com.itea.practice.fundamentals.task.components.data.InternetReceiver;

public class FundamentalsApp extends Application {
    private InternetReceiver internetReceiver;
    private PingStatusController stateController;
    private PingHistoryController historyController;

    @Override
    public void onCreate() {
        super.onCreate();

        this.internetReceiver = new InternetReceiver();
        this.stateController = new PingStatusController(this, internetReceiver);
        this.historyController = new PingHistoryController(this);

        registerInternetReceiver();
    }

    private void registerInternetReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(internetReceiver, filter);
    }

    public InternetReceiver getInternetReceiver() {
        return internetReceiver;
    }

    public PingStatusController getStateController() {
        return stateController;
    }

    public PingHistoryController getHistoryController() {
        return historyController;
    }

}
