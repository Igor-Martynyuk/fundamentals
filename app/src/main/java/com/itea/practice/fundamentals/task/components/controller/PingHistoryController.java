package com.itea.practice.fundamentals.task.components.controller;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.itea.practice.fundamentals.task.components.data.PingHistoryProvider;

public class PingHistoryController {
    private Context context;

    public PingHistoryController(Context context) {
        this.context = context;

        context.getContentResolver().registerContentObserver(
                PingHistoryProvider.HISTORY_URI,
                true,
                new ContentObserver(new Handler(Looper.getMainLooper())) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);

                    }
                }
        );
    }

    //    @SuppressLint("SetTextI18n")
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

}
