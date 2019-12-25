package com.itea.practice.fundamentals.task;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itea.practice.components.PingFilter;
import com.itea.practice.components.PingLog;
import com.itea.practice.components.PingStatsStorage;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.List;

public class PingHistoryProvider extends ContentProvider {
    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_FAILURE = 0;

    public final static String SELECTION_FILTERED = "filtered";

    public final static String SEGMENT_HISTORY = "history";

    public final static String FIELD_RESULT = "field_result";
    public final static String FIELD_DURATION = "field_duration";
    public final static String FIELD_DATE = "field_date";

    public final static String SCHEME = "content";
    public final static String AUTHORITIES = "com.itea.practice.fundamentals.ping";

    public final static Uri HISTORY_URI = Uri.parse(
            PingHistoryProvider.SCHEME + "://" + PingHistoryProvider.AUTHORITIES + "/" + SEGMENT_HISTORY
    );

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

    public Cursor getCursor(final String[] projection, final String selection, final int index) {
        return new AbstractCursor() {
            private final List<PingLog> data = storage.filter(
                    new PingFilter() {
                        private boolean checkIndex(PingLog current) {
                            return index < 0 || storage.getLogs().indexOf(current) == index;
                        }

                        @Override
                        public boolean filter(PingLog current) {
                            if (selection == null || selection.isEmpty()) {
                                return checkIndex(current);
                            } else if (selection.equals(SELECTION_FILTERED)) {
                                Calendar limit = Calendar.getInstance();
                                limit.add(Calendar.MONTH, -1);

                                Calendar target = Calendar.getInstance();
                                target.setTimeInMillis(current.getDate());

                                return target.after(limit) && current.getResult() && checkIndex(current);
                            } else {
                                throw new IllegalArgumentException("Invalid selection argument");
                            }

                        }
                    }
            );


            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public String[] getColumnNames() {
                if (projection.length == 0) {
                    return new String[]{FIELD_RESULT, FIELD_DURATION, FIELD_DATE};
                } else {
                    return projection;
                }
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
                String fieldName = getColumnNames()[column];

                if (!fieldName.equals(FIELD_RESULT)) {
                    throw new InvalidParameterException("Field " + fieldName + "is not represented as Int");
                }

                return data.get(this.getPosition()).getResult()
                        ? RESULT_SUCCESS
                        : RESULT_FAILURE;
            }

            @Override
            public long getLong(int column) {
                String fieldName = getColumnNames()[column];
                PingLog row = data.get(getPosition());

                switch (fieldName) {
                    case FIELD_DATE:
                        return row.getDate();
                    case FIELD_DURATION:
                        return row.getDuration();
                    default:
                        throw new InvalidParameterException(
                                "Field " + fieldName + "is not represented as Long"
                        );
                }
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
                return false;
            }
        };
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

        if (uri.getScheme() == null || uri.getPath() == null || uri.getAuthority() == null) {
            throw new IllegalArgumentException("Wrong URI");
        }

        if (!uri.getScheme().equals(SCHEME)) {
            throw new IllegalArgumentException("Wrong Schema");
        }

        if (!uri.getAuthority().equals(AUTHORITIES)) {
            throw new IllegalArgumentException("Wrong authorities");
        }

        if (uri.getPath().contains(SEGMENT_HISTORY)) {
            List<String> pathSegments = uri.getPathSegments();

            switch (pathSegments.size()) {
                case 1:
                    return getCursor(
                            projection,
                            selection,
                            -1
                    );
                case 2:
                    return getCursor(
                            projection,
                            selection,
                            Integer.valueOf(pathSegments.get(pathSegments.size() - 1))
                    );
                default:
                    throw new UnsupportedOperationException();
            }

        } else {
            throw new UnsupportedOperationException();
        }
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
        if (values == null) return null;

        if (uri.getPathSegments().get(0).equals(SEGMENT_HISTORY)) {
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
