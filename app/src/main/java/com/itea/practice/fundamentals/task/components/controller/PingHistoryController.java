package com.itea.practice.fundamentals.task.components.controller;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.task.components.data.PingHistoryProvider;

import java.util.ArrayList;
import java.util.List;

//        @SuppressLint("SetTextI18n")
//    public void updateDelay() {
//
//        Cursor cursor = getContentResolver().query(
//                PingHistoryProvider.HISTORY_URI,
//                new String[]{PingHistoryProvider.FIELD_DURATION},
//                PingHistoryProvider.SELECTION_FILTERED,
//                null,
//                null
//        );
//
//        long delaySum = 0L;
//        long commonDelay = 0L;
//        long lastDelay = 0L;
//
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//
//            while (!cursor.isAfterLast()) {
//                long delay = cursor.getLong(0);
//
//                delaySum += delay;
//                lastDelay = delay;
//
//                cursor.moveToNext();
//            }
//            commonDelay = delaySum / cursor.getCount();
//
//            cursor.close();
//        }
//
//        outputDelay.setText(
//                lastDelay
//                        + "ms "
//                        + getString(R.string.last).toLowerCase()
//                        + "\n"
//                        + commonDelay
//                        + "ms "
//                        + getString(R.string.common).toLowerCase()
//        );
//    }

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

    public void addCommonDelayListener(CommonDelayListener listener) {
        delayListeners.add(listener);
    }

    public void removeCommonDelayListener(CommonDelayListener listener) {
        delayListeners.remove(listener);
    }

    public void addLogReceivedListener(LogReceivedListener listener) {
        logReceivedListeners.add(listener);
    }

    public void removeLogReceivedListener(LogReceivedListener listener){
        logReceivedListeners.remove(listener);
    }

    interface CommonDelayListener {
        void onCommonDelayChanged(long value);
    }

    interface LogReceivedListener {
        void onLogReceived(PingLog value);
    }

    private class HistoryObserver extends ContentObserver {

        HistoryObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    }

}
