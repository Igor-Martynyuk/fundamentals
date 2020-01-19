package com.itea.practice.fundamentals.task.components.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.itea.practice.components.PingCallBack;
import com.itea.practice.components.PingExecutor;
import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.task.components.data.provider.PingHistoryUriBuilder;
import com.itea.practice.fundamentals.task.components.data.provider.MapperLogToValues;

public class PingService extends Service {
    private PingServiceBinder binder;
    private PingExecutor executor;
    private PingCallBack callBack;

    @Override
    public void onCreate() {
        this.executor = new PingExecutor();
        this.callBack = new CallBack();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new PingServiceBinder() {

                @Override
                public void startPingProcess() {
                    executor.execute(callBack);
                }

                @Override
                public void stopPingProcess() {
                    executor.interrupt();
                }
            };
        }

        return binder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private class CallBack implements PingCallBack {
        private void sendPingLog(long started, long finished) {
            PingLog log = new PingLog(true, finished - started, started);
            getContentResolver().insert(PingHistoryUriBuilder.build(), MapperLogToValues.map(log));
        }

        @Override
        public void onSuccess(long started, long finished) {
            sendPingLog(started, finished);
        }

        @Override
        public void onFailure(long started, long finished) {
            sendPingLog(started, finished);
        }
    }

}
