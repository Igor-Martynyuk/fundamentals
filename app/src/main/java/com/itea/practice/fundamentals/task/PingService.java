package com.itea.practice.fundamentals.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.itea.practice.components.PingExecutor;
import com.itea.practice.components.PingLog;

public class PingService extends Service {
    private PingExecutor executor;
    private PingServiceBinder binder;

    private PingExecutor.CallBack callBack = new PingExecutor.CallBack() {
        @Override
        public void onSuccess(long started, long finished) {
            binder.onPing(new PingLog(true, finished - started, started));
        }

        @Override
        public void onFailure(long started, long finished) {
            binder.onPing(new PingLog(false, finished - started, started));
        }
    };

    @Override
    public void onCreate() {
        this.executor = new PingExecutor();
        this.executor.execute(this.callBack, "8.8.8.8");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new PingServiceBinder();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binder = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        this.executor.interrupt();

        super.onDestroy();
    }

}
