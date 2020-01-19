package com.itea.practice.fundamentals.task.components.manager.internet;

import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.task.components.data.receiver.InternetReceiver;

import java.util.ArrayList;
import java.util.List;

public class InternetManager {
    private List<InternetListener> listeners;

    public InternetManager(InternetReceiver receiver) {
        this.listeners = new ArrayList<>();

        receiver.addListener(new Listener());
    }

    public void addListener(InternetListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InternetListener listener) {
        listeners.remove(listener);
    }

    class Listener implements InternetReceiver.Listener {
        private InternetState currentState;

        private InternetState determineInternetState(@Nullable String typeStr) {
            if (typeStr == null) return InternetState.NONE;
            if (typeStr.equals("WIFI")) return InternetState.WIFI;
            if (typeStr.equals("MOBILE")) return InternetState.MOBILE;

            throw new IllegalArgumentException("Illegal connection type");
        }

        @Override
        public void onInternetChanged(@Nullable String type) {
            InternetState newState = determineInternetState(type);

            if (newState != currentState) {
                currentState = newState;

                for (InternetListener listener : listeners) {
                    listener.onInternetStateChanged(currentState);
                }
            }

        }
    }
}
