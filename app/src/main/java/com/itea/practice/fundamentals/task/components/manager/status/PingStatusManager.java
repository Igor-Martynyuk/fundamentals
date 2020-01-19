package com.itea.practice.fundamentals.task.components.manager.status;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.task.components.data.receiver.InternetReceiver;
import com.itea.practice.fundamentals.task.components.data.service.PingService;
import com.itea.practice.fundamentals.task.components.data.service.PingServiceBinder;

import java.util.ArrayList;
import java.util.List;

public class PingStatusManager {
    private Context context;
    private InternetReceiver internetReceiver;
    private InternetListener internetListener;
    private String currentConnectionType;
    private PingServiceBinder serviceBinder;
    private PingServiceConnection serviceConnection;
    private List<StatusListener> statusListeners;
    private Status currentStatus;

    public PingStatusManager(Context context, InternetReceiver internetReceiver) {
        this.context = context;

        this.internetReceiver = internetReceiver;
        this.internetListener = new InternetListener();
        this.currentConnectionType = null;

        this.serviceConnection = new PingServiceConnection();
        this.statusListeners = new ArrayList<>();
        this.currentStatus = Status.NONE;
    }

    private void updateStatus(Status value) {
        for (StatusListener listener : statusListeners) {
            listener.onPingStatusChanged(value);
        }

        currentStatus = value;
    }

    private void dispose() {
        serviceBinder = null;

        internetReceiver.removeListener(internetListener);
        currentConnectionType = null;

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

    public void addStatusListener(StatusListener listener) {
        statusListeners.add(listener);
        listener.onPingStatusChanged(currentStatus);
    }

    public void removeStatusListener(StatusListener listener) {
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

    private class InternetListener implements InternetReceiver.Listener {
        @Override
        public void onInternetChanged(@Nullable String type) {
            if (currentConnectionType == null && type != null) {
                serviceBinder.startPingProcess();
                updateStatus(Status.ACTIVE);
            }

            if (currentConnectionType != null && type == null) {
                serviceBinder.stopPingProcess();
                updateStatus(Status.STARTED);
            }

            currentConnectionType = type;
        }
    }

    public enum Status {
        NONE,
        STARTED,
        ACTIVE
    }
}
