package com.itea.practice.components;

import android.os.Handler;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class PingExecutor {
    private volatile AtomicReference<PingCallBack> callBack = new AtomicReference<>();

    private boolean isActive = false;
    private Thread worker;
    private Handler handler = new Handler();

    private void notify(final boolean result, final long started) {
        if (callBack.get() == null) return;

        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
                            callBack.get().onSuccess(started, System.currentTimeMillis());
                        } else {
                            callBack.get().onFailure(started, System.currentTimeMillis());
                        }
                    }
                }
        );
    }

    private void start() {
        while (isActive) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final long started = System.currentTimeMillis();
            try {

                URL url = new URL("https://www.google.com:443/");
                String hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();

                Socket socket = new Socket(hostAddress, url.getPort());
                socket.close();

                notify(true, started);

            } catch (Exception error) {

                error.printStackTrace();
                notify(false, started);

            }
        }
    }

    public void execute(PingCallBack callBack) {
        if (isActive) return;

        this.isActive = true;
        this.callBack.set(callBack);

        this.worker = new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });

        this.worker.start();
    }

    public void interrupt() {
        if (!this.isActive) return;

        this.isActive = false;
        this.worker.interrupt();

        this.callBack.set(null);
    }

    public boolean isActive() {
        return isActive;
    }

}
