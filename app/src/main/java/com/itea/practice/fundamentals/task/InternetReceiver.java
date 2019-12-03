package com.itea.practice.fundamentals.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.itea.practice.components.InternetResolver;

import java.util.ArrayList;
import java.util.List;

public class InternetReceiver extends BroadcastReceiver {
    private List<Listener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        InternetResolver resolver = new InternetResolver(context);

        for (Listener listener : this.listeners){
            listener.onInternetChanged(resolver.determineConnection());
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public interface Listener {
        void onInternetChanged(@Nullable String type);
    }
}
