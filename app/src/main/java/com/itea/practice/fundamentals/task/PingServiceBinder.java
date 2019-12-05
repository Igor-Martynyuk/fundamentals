package com.itea.practice.fundamentals.task;

import android.os.Binder;

import com.itea.practice.components.PingLog;

public class PingServiceBinder extends Binder {
    private PingListener pingListener;

    void onPing(PingLog log) {
        this.pingListener.onPing(log);
    }

    void setOnPingListener(PingListener listener) {
        this.pingListener = listener;
    }

    interface PingListener {
        void onPing(PingLog log);
    }

}
