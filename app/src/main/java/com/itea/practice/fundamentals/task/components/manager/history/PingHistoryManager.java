package com.itea.practice.fundamentals.task.components.manager.history;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.task.components.data.provider.PingHistoryProvider;
import com.itea.practice.fundamentals.task.components.data.provider.PingHistoryUriBuilder;

import java.util.ArrayList;
import java.util.List;

public class PingHistoryManager {
    private final String[] projection = new String[]{
            PingHistoryProvider.RESULT,
            PingHistoryProvider.DURATION,
            PingHistoryProvider.DATE
    };

    private Context context;
    private List<CommonDurationListener> delayListeners;
    private List<LogReceivedListener> logReceivedListeners;

    public PingHistoryManager(Context context) {
        this.context = context;

        this.delayListeners = new ArrayList<>();
        this.logReceivedListeners = new ArrayList<>();

        //Ассинхронка. Пока не партесь
        Handler handler = new Handler(Looper.getMainLooper());
        ContentObserver observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);

                onNewLog(uri);
            }
        };

        context.getContentResolver().registerContentObserver(
                PingHistoryUriBuilder.build(),
                true,
                observer
        );
    }

    private PingLog parseLog(Cursor cursor) {
        return new PingLog(
                cursor.getInt(0) == PingHistoryProvider.SUCCESS,
                cursor.getLong(1),
                cursor.getLong(2)
        );
    }

    @Nullable
    private List<PingLog> requestAll(String filter, String[] projection) {
        Uri uri = PingHistoryUriBuilder.build();
        Cursor cursor = context.getContentResolver().query(uri, projection, filter, null, null);


        if (cursor == null || cursor.getCount() <= 0) return null;

        List<PingLog> result = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            result.add(parseLog(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return result;
    }

    private PingLog request(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        PingLog result = null;
        if (cursor != null && cursor.getCount() >= 1) {
            cursor.moveToFirst();
            result = parseLog(cursor);
            cursor.close();
        }

        return result;
    }

    private void onNewLog(Uri uri) {
        PingLog log = request(uri);
        if (log == null) return;

        for (CommonDurationListener listener : delayListeners) {
            listener.onCommonDurationChanged(getCommonDuration());
        }

        for (LogReceivedListener listener : logReceivedListeners) {
            listener.onPingReceived(log);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public long getCommonDuration() throws NullPointerException {
        List<PingLog> logs = requestAll(PingHistoryProvider.FILTER_SUCCESS_ONLY, projection);

        long result = 0L;
        for (PingLog log : logs) {
            result += log.getDuration();
        }

        return result / logs.size();
    }

    public Long getLastSuccessfulDuration() throws NullPointerException {
        List<PingLog> logs = requestAll(PingHistoryProvider.FILTER_SUCCESS_ONLY, projection);
        PingLog last = logs.get(logs.size() - 1);

        return last.getDuration();
    }

    public void addCommonDelayListener(CommonDurationListener listener) {
        delayListeners.add(listener);
    }

    public void removeCommonDelayListener(CommonDurationListener listener) {
        delayListeners.remove(listener);
    }

    public List<PingLog> getLogs() {
        List<PingLog> result = requestAll(null, projection);
        return result == null ? new ArrayList<PingLog>() : result;
    }

    public void addLogReceivedListener(LogReceivedListener listener) {
        logReceivedListeners.add(listener);
    }

    public void removeLogReceivedListener(LogReceivedListener listener) {
        logReceivedListeners.remove(listener);
    }

}
