package com.itea.practice.fundamentals.task.components.controller;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.task.components.data.PingHistoryProvider;

import java.util.ArrayList;
import java.util.List;

public class PingHistoryController {
    private Context context;

    private List<CommonDelayListener> delayListeners;
    private List<LogReceivedListener> logReceivedListeners;

    public PingHistoryController(Context context) {
        this.context = context;

        this.delayListeners = new ArrayList<>();
        this.logReceivedListeners = new ArrayList<>();

        context.getContentResolver().registerContentObserver(
                PingHistoryProvider.HISTORY_URI,
                true,
                new HistoryObserver()
        );
    }

    private long getDelay() {
        Cursor cursor = context.getContentResolver().query(
                PingHistoryProvider.HISTORY_URI,
                new String[]{PingHistoryProvider.FIELD_DURATION},
                PingHistoryProvider.SELECTION_FILTERED,
                null,
                null
        );

        long delaySum = 0L;
        long result = 0L;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                long delay = cursor.getLong(0);

                delaySum += delay;

                cursor.moveToNext();
            }

            result = delaySum / cursor.getCount();
            cursor.close();
        }

        return result;
    }

    @Nullable
    private PingLog getLastLog() {
        Cursor cursor = context.getContentResolver().query(
                PingHistoryProvider.HISTORY_URI,
                null,
                PingHistoryProvider.SELECTION_FILTERED,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToLast();

        PingLog result = new PingLog(
                cursor.getInt(0) == PingHistoryProvider.RESULT_SUCCESS,
                cursor.getLong(1),
                cursor.getLong(2)
        );
        cursor.close();
        return result;
    }

    public void addCommonDelayListener(CommonDelayListener listener) {
        delayListeners.add(listener);

        listener.onCommonDelayChanged(getDelay());
    }

    public void removeCommonDelayListener(CommonDelayListener listener) {
        delayListeners.remove(listener);
    }

    public void addLogReceivedListener(LogReceivedListener listener) {
        logReceivedListeners.add(listener);

        PingLog log = getLastLog();
        if (log == null) return;
        listener.onLogReceived(log);
    }

    public void removeLogReceivedListener(LogReceivedListener listener) {
        logReceivedListeners.remove(listener);
    }

    public interface CommonDelayListener {
        void onCommonDelayChanged(long value);
    }

    public interface LogReceivedListener {
        void onLogReceived(PingLog value);
    }

    private class HistoryObserver extends ContentObserver {

        HistoryObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            long delay = getDelay();
            PingLog log = getLastLog();
            if (log == null) return;

            for (CommonDelayListener listener : delayListeners) {
                listener.onCommonDelayChanged(delay);
            }

            for (LogReceivedListener listener : logReceivedListeners) {
                listener.onLogReceived(log);
            }
        }
    }

}
