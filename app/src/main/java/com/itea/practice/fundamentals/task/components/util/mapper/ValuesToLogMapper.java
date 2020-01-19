package com.itea.practice.fundamentals.task.components.util.mapper;

import android.content.ContentValues;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.Const;

public class ValuesToLogMapper {

    public static PingLog map(ContentValues values) {
        return new PingLog(
                values.getAsInteger(Const.FIELD_RESULT) == Const.RESULT_SUCCESS,
                values.getAsLong(Const.FIELD_DURATION),
                values.getAsLong(Const.FIELD_DATE)
        );
    }

}
