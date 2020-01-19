package com.itea.practice.fundamentals.task.components.util.mapper;

import android.content.ContentValues;

import com.itea.practice.components.PingLog;
import com.itea.practice.fundamentals.Const;

public class LogToValuesMapper {

    public static ContentValues map(PingLog log) {
        ContentValues result = new ContentValues();

        result.put(Const.FIELD_RESULT, log.getResult() ? Const.RESULT_SUCCESS : Const.RESULT_FAILURE);
        result.put(Const.FIELD_DURATION, log.getDuration());
        result.put(Const.FIELD_DATE, log.getDate());

        return result;
    }

}
