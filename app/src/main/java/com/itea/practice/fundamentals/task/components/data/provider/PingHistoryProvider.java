package com.itea.practice.fundamentals.task.components.data.provider;

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
    public final static int SUCCESS = 1;
    public final static int FAILURE = 0;

    public final static String RESULT = "field_result";
    public final static String DURATION = "field_duration";
    public final static String DATE = "field_date";

    public final static String SCHEME = "content";
    public final static String AUTHORITY = "com.itea.practice.fundamentals.ping.history";

    public final static String FILTER_SUCCESS_ONLY = "filter_success";

    private PingStatsStorage storage;

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
                            } else if (selection.equals(FILTER_SUCCESS_ONLY)) {
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
                if (projection == null || projection.length == 0) {
                    return new String[]{RESULT, DURATION, DATE};
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

                if (!fieldName.equals(RESULT)) {
                    throw new InvalidParameterException("Field " + fieldName + "is not represented as Int");
                }

                return data.get(this.getPosition()).getResult()
                        ? SUCCESS
                        : FAILURE;
            }

            @Override
            public long getLong(int column) {
                String fieldName = getColumnNames()[column];
                PingLog row = data.get(getPosition());

                switch (fieldName) {
                    case DATE:
                        return row.getDate();
                    case DURATION:
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

        if (!uri.getAuthority().equals(AUTHORITY)) {
            throw new IllegalArgumentException("Wrong authorities");
        }

        List<String> pathSegments = uri.getPathSegments();

        switch (pathSegments.size()) {
            case 0:
                return getCursor(
                        projection,
                        selection,
                        -1
                );
            case 1:
                return getCursor(
                        projection,
                        selection,
                        Integer.valueOf(pathSegments.get(pathSegments.size() - 1))
                );
            default:
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

        if (values != null && AUTHORITY.equals(uri.getAuthority())) {
            PingLog log = MapperValuesToLog.map(values);
            storage.insertLog(log);

            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(
                        PingHistoryUriBuilder.build(storage.getLogs().indexOf(log)),
                        null
                );
            }
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
