package com.itea.practice.fundamentals.task;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itea.practice.components.PingLog;
import com.itea.practice.components.PingStatsStorage;

import java.security.InvalidParameterException;

public class PingHistoryProvider extends ContentProvider {
    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_FAILURE = 0;

    public final static String FIELD_RESULT = "field_result";
    public final static String FIELD_DURATION = "field_duration";
    public final static String FIELD_DATE = "field_date";

    public final static String SCHEME = "content://";
    public final static String AUTHORITIES = "com.itea.practice.fundamentals.ping";

    private PingStatsStorage storage;

    public static ContentValues logToValues(PingLog log) {
        ContentValues result = new ContentValues();

        result.put(FIELD_RESULT, log.getResult() ? RESULT_SUCCESS : RESULT_FAILURE);
        result.put(FIELD_DURATION, log.getDuration());
        result.put(FIELD_DATE, log.getDate());

        return result;
    }

    public static PingLog valuesToLog(ContentValues values) {
        return new PingLog(
                values.getAsInteger(FIELD_RESULT) == RESULT_SUCCESS,
                values.getAsLong(FIELD_DURATION),
                values.getAsLong(FIELD_DATE)
        );
    }

    @Override
    public boolean onCreate() {
        if (getContext() == null) return false;

        this.storage = new PingStatsStorage(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {


        return new AbstractCursor() {
            @Override
            public int getCount() {
                return storage.getLogs().size();
            }

            @Override
            public String[] getColumnNames() {
                return new String[]{FIELD_RESULT, FIELD_DURATION, FIELD_DATE};
            }

            @Override
            public String getString(int column) {
                throw new UnsupportedOperationException();
            }

            @Override
            public short getShort(int column) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getInt(int column) {
                if (column == 0) {
                    return storage.getLogs().get(this.getPosition()).getResult()
                            ? RESULT_SUCCESS
                            : RESULT_FAILURE;
                }

                throw new InvalidParameterException();
            }

            @Override
            public long getLong(int column) {
                PingLog row = storage.getLog(column);

                if (column == 1) return row.getDuration();
                if (column == 2) return row.getDate();

                throw new InvalidParameterException();
            }

            @Override
            public float getFloat(int column) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getDouble(int column) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isNull(int column) {
                return true;
            }
        };
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {

        if (values != null) {
            storage.insertLog(valuesToLog(values));
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        throw new UnsupportedOperationException();
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        throw new UnsupportedOperationException();
    }

}
