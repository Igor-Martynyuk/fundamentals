package com.itea.practice.fundamentals.task.components.manager.status;

public interface StatusListener {
    void onPingStatusChanged(PingStatusManager.Status value);
}
