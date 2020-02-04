package com.itea.practice.fundamentals.task.data.source.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHumansHelper extends SQLiteOpenHelper {
    private final String TABLE_NAMES = "names";
    private final String FIELD_ID = "_id";
    private final String FIELD_NAME = "name";

    SQLiteHumansHelper(Context context) {
        super(context, "humansDB", null, 1);
    }

    Cursor loadAll() {
        return getWritableDatabase().query(
                TABLE_NAMES,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    boolean putName(String value) {
        ContentValues values = new ContentValues();
        values.putNull(FIELD_ID);
        values.put(FIELD_NAME, value);

        return getWritableDatabase().insert(TABLE_NAMES, null, values) > -1;
    }

    boolean dropHumansTable() {
        return getWritableDatabase().delete(TABLE_NAMES, "", null) > 0;
    }

    boolean removeHumanById(long id) {
        int removed = getWritableDatabase().delete(
                TABLE_NAMES,
                FIELD_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return removed > 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists "
                        + TABLE_NAMES
                        + " ("
                        + FIELD_ID
                        + " integer primary key autoincrement, "
                        + FIELD_NAME
                        + " text);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {/*nothing*/}
}