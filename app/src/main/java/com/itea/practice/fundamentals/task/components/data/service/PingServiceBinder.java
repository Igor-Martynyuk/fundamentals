package com.itea.practice.fundamentals.task.components.data.service;

import android.os.Binder;

public abstract class PingServiceBinder extends Binder {
    public abstract void startPingProcess();

    public abstract void stopPingProcess();
}
