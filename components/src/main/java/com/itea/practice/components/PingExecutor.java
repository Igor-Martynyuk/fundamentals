package com.itea.practice.components;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class PingExecutor {
    private volatile AtomicReference<String> address = new AtomicReference<>();
    private volatile AtomicReference<CallBack> callBack = new AtomicReference<>();

    private boolean isActive = false;
    private Thread worker;
    private Handler handler = new Handler(Looper.getMainLooper());

    public void execute(CallBack callBack, String address) {
        if (isActive) return;

        this.isActive = true;
        this.callBack.set(callBack);
        this.address.set(address);

        this.worker = new Thread(new Action());
        this.worker.start();
    }

    public void interrupt() {
        if (!this.isActive) return;

        this.isActive = false;
        this.worker.interrupt();

        this.callBack.set(null);
        this.address.set(null);
    }

    public boolean isActive() {
        return isActive;
    }

    private class Action implements Runnable {
        @Override
        public void run() {
            while (isActive) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final long started = System.currentTimeMillis();

                try {

                    if (address == null) continue;
                    Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + address);

                    final int result = process.waitFor();
                    final long finished = System.currentTimeMillis();

                    if (callBack.get() == null) continue;

                    handler.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (result == 0) {
                                        callBack.get().onSuccess(started, finished);
                                    } else {
                                        callBack.get().onFailure(started, finished);
                                    }
                                }
                            }
                    );

                } catch (InterruptedException | IOException error) {
                    if (error instanceof InterruptedException) {
                        break;
                    } else {
                        error.printStackTrace();
                        callBack.get().onFailure(started, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public interface CallBack {
        void onSuccess(long started, long finished);

        void onFailure(long started, long finished);
    }

}
