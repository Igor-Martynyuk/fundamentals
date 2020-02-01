package com.itea.practice.fundamentals.task.components.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InternetReceiver extends BroadcastReceiver {
    private String currentType;
    private volatile List<Listener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        currentType = new InternetResolver(context).determineConnection();

        for (Listener listener : this.listeners) {
            listener.onInternetChanged(currentType);
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);

        listener.onInternetChanged(currentType);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public interface Listener {
        void onInternetChanged(@Nullable String type);
    }
}
