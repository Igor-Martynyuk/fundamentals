package com.itea.practice.fundamentals;

import android.app.Application;
import android.content.IntentFilter;

import com.itea.practice.fundamentals.task.components.data.receiver.InternetReceiver;
import com.itea.practice.fundamentals.task.components.manager.history.PingHistoryManager;
import com.itea.practice.fundamentals.task.components.manager.internet.InternetManager;
import com.itea.practice.fundamentals.task.components.manager.status.PingStatusManager;

public class FundamentalsApp extends Application {
    private InternetReceiver internetReceiver;
    private InternetManager internetManager;
    private PingStatusManager pingStateManager;
    private PingHistoryManager pingHistoryManager;

    @Override
    public void onCreate() {
        super.onCreate();

        this.internetReceiver = new InternetReceiver();
        this.internetManager = new InternetManager(internetReceiver);
        this.pingStateManager = new PingStatusManager(this, internetReceiver);
        this.pingHistoryManager = new PingHistoryManager(this);

        registerInternetReceiver();
    }

    private void registerInternetReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(internetReceiver, filter);
    }

    public InternetManager getInternetManager() {
        return internetManager;
    }

    public PingStatusManager getPingStateManager() {
        return pingStateManager;
    }

    public PingHistoryManager getPingHistoryManager() {
        return pingHistoryManager;
    }

}
