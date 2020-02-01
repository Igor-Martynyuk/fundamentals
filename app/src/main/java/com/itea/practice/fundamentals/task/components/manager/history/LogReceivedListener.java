package com.itea.practice.fundamentals.task.components.manager.history;

import com.itea.practice.fundamentals.task.components.data.PingLog;

public interface LogReceivedListener {
    void onPingReceived(PingLog value);
}