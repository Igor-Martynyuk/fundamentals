package com.itea.practice.fundamentals.task.components.data;

import android.os.Binder;

import androidx.annotation.CallSuper;

import com.itea.practice.components.PingLog;

public abstract class PingServiceBinder extends Binder {
    private PingListener pingListener;

    void onPing(PingLog log) {
        if (pingListener != null) {
            this.pingListener.onPing(log);
        }
    }

    public abstract boolean isActive();

    public abstract void startPingProcess();

    public abstract void stopPingProcess();

    public void setOnPingListener(PingListener listener) {
        this.pingListener = listener;
    }

    public interface ExecutionStateListener {
        void onStateChanged(boolean state);
    }

    public interface PingListener {
        void onPing(PingLog log);
    }

}
