package com.itea.practice.fundamentals.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.itea.practice.components.PingCallBack;
import com.itea.practice.components.PingExecutor;
import com.itea.practice.components.PingLog;

public class PingService extends Service {
    private PingExecutor executor;
    private PingServiceBinder binder;

    private PingCallBack callBack = new PingCallBack() {
        @Override
        public void onSuccess(long started, long finished) {
            if (binder != null) {
                binder.onPing(new PingLog(true, finished - started, started));
            }
        }

        @Override
        public void onFailure(long started, long finished) {
            if (binder != null) {
                binder.onPing(new PingLog(false, finished - started, started));
            }
        }
    };

    @Override
    public void onCreate() {
        this.executor = new PingExecutor();

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
                boolean isActive() {
                    return executor.isActive();
                }

                @Override
                void startPingProcess() {
                    super.startPingProcess();

                    executor.execute(callBack);
                }

                @Override
                void stopPingProcess() {
                    super.stopPingProcess();

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

}
