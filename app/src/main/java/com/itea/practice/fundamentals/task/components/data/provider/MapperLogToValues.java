package com.itea.practice.fundamentals.task.components.data.provider;

import android.content.ContentValues;

import com.itea.practice.fundamentals.task.components.data.PingLog;

public class MapperLogToValues {

    public static ContentValues map(PingLog log) {
        ContentValues result = new ContentValues();

        result.put(PingHistoryProvider.RESULT, log.getResult() ? PingHistoryProvider.SUCCESS : PingHistoryProvider.FAILURE);
        result.put(PingHistoryProvider.DURATION, log.getDuration());
        result.put(PingHistoryProvider.DATE, log.getDate());

        return result;
    }

}
