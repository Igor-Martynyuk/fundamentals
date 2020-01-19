package com.itea.practice.fundamentals.task.components.data.provider;

import android.content.ContentValues;

import com.itea.practice.components.PingLog;

public class MapperValuesToLog {

    public static PingLog map(ContentValues values) {

        return new PingLog(
                values.getAsInteger(PingHistoryProvider.RESULT) == PingHistoryProvider.SUCCESS,
                values.getAsLong(PingHistoryProvider.DURATION),
                values.getAsLong(PingHistoryProvider.DATE)
        );

    }

}
