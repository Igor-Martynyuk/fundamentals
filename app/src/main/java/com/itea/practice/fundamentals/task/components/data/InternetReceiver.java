package com.itea.practice.fundamentals.task.components.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itea.practice.components.InternetResolver;

import java.util.ArrayList;
import java.util.List;

public class InternetReceiver extends BroadcastReceiver {
    private volatile List<Listener> listeners = new ArrayList<>();
    private Type lastType = Type.NONE;

    @Override
    public void onReceive(Context context, Intent intent) {
        InternetResolver resolver = new InternetResolver(context);
        String typeValue = resolver.determineConnection();

        Type currentType = Type.NONE;
        if (typeValue != null) {
            currentType = typeValue.equals("WIFI") ? Type.WIFI : Type.MOBILE;
        }

        if (currentType != lastType) {
            for (Listener listener : this.listeners) {
                listener.onInternetChanged(currentType);
            }
        }

        lastType = currentType;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);

        listener.onInternetChanged(lastType);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public interface Listener {
        void onInternetChanged(Type type);
    }

    public enum Type {
        NONE,
        MOBILE,
        WIFI
    }
}
