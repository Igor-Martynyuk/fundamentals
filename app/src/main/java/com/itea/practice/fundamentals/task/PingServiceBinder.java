package com.itea.practice.fundamentals.task;

import android.os.Binder;

import androidx.annotation.CallSuper;

import com.itea.practice.components.PingLog;

abstract class PingServiceBinder extends Binder {
    private PingListener pingListener;
    private ExecutionStateListener stateListener;

    void onPing(PingLog log) {
        this.pingListener.onPing(log);
    }

    abstract boolean isActive();

    @CallSuper
    void startPingProcess(){
        this.stateListener.onStateChanged(true);
    }

    @CallSuper
    void stopPingProcess(){
        this.stateListener.onStateChanged(false);
    }

    void setOnPingListener(PingListener listener) {
        this.pingListener = listener;
    }

    void setStateListener(ExecutionStateListener listener) {
        this.stateListener = listener;
    }

    interface ExecutionStateListener {
        void onStateChanged(boolean state);
    }

    interface PingListener {
        void onPing(PingLog log);
    }

}
