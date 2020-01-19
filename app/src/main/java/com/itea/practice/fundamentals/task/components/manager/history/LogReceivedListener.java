package com.itea.practice.fundamentals.task.components.manager.history;

import com.itea.practice.components.PingLog;

public interface LogReceivedListener {
    void onPingReceived(PingLog value);
}