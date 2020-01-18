package com.itea.practice.fundamentals.task.components.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.itea.practice.fundamentals.task.components.data.PingInternetReceiver;
import com.itea.practice.fundamentals.task.components.data.PingService;
import com.itea.practice.fundamentals.task.components.data.PingServiceBinder;

import java.util.ArrayList;
import java.util.List;

public class PingStatusController {
    private Context context;
    private PingInternetReceiver internetReceiver;
    private InternetListener internetListener;
    private PingInternetReceiver.Type currentConnectionType;
    private PingServiceBinder serviceBinder;
    private PingServiceConnection serviceConnection;
    private List<Listener> statusListeners;
    private Status currentStatus;

    public PingStatusController(Context context, PingInternetReceiver internetReceiver) {
        this.context = context;

        this.internetReceiver = internetReceiver;
        this.internetListener = new InternetListener();
        this.currentConnectionType = PingInternetReceiver.Type.NONE;

        this.serviceConnection = new PingServiceConnection();
        this.statusListeners = new ArrayList<>();
        this.currentStatus = Status.NONE;
    }

    private void updateStatus(Status value) {
        for (Listener listener : statusListeners) {
            listener.onPingStatusChanged(value);
        }

        currentStatus = value;
    }

    private void dispose() {
        serviceBinder = null;

        internetReceiver.removeListener(internetListener);
        currentConnectionType = PingInternetReceiver.Type.NONE;

        updateStatus(Status.NONE);
    }

    public void onStartCommand() {
        serviceConnection = new PingServiceConnection();
        context.startService(new Intent(context, PingService.class));
        context.bindService(new Intent(context, PingService.class), serviceConnection, 0);
    }

    public void onStopCommand() {
        serviceBinder.stopPingProcess();
        context.unbindService(serviceConnection);
        context.stopService(new Intent(context, PingService.class));

        dispose();
    }

    public void addStatusListener(Listener listener) {
        statusListeners.add(listener);
        listener.onPingStatusChanged(currentStatus);
    }

    public void removeStatusListener(Listener listener) {
        statusListeners.remove(listener);
    }

    private class PingServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceBinder = (PingServiceBinder) service;
            internetReceiver.addListener(internetListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dispose();
        }
    }

    private class InternetListener implements PingInternetReceiver.Listener {
        @Override
        public void onInternetChanged(PingInternetReceiver.Type type) {
            if (currentConnectionType == PingInternetReceiver.Type.NONE && type != PingInternetReceiver.Type.NONE) {
                serviceBinder.startPingProcess();
                updateStatus(Status.ACTIVE);
            }

            if (currentConnectionType != PingInternetReceiver.Type.NONE && type == PingInternetReceiver.Type.NONE) {
                serviceBinder.stopPingProcess();
                updateStatus(Status.STARTED);
            }

            currentConnectionType = type;
        }
    }

    public interface Listener {
        void onPingStatusChanged(Status value);
    }

    public enum Status {
        NONE,
        STARTED,
        ACTIVE
    }
}
