package com.itea.practice.fundamentals.task.data.source.commons.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

import com.itea.practice.fundamentals.task.data.source.commons.CommonSourceFragment;

public class SQLiteFragment extends CommonSourceFragment {
    private DbHelper helper;
    private CursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DbHelper();

    }

    private void updateList() {
        output.setAdapter(
                adapter = new SimpleCursorAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        helper.loadAll(),
                        new String[]{"name"},
                        new int[]{android.R.id.text1}
                )
        );
    }

    @Override
    protected void onClearBtnPressed() {
        helper.dropHumansTable();
        updateList();
    }

    @Override
    protected void onInputFinished() {
        ContentValues values = new ContentValues();
        values.put("name", input.getText().toString());
        helper.putName(values);

        updateList();
    }

    private class DbHelper extends SQLiteOpenHelper {
        private SQLiteDatabase database = getWritableDatabase();

        DbHelper() {
            super(SQLiteFragment.this.requireContext(), "humans", null, 1);
        }

        Cursor loadAll() {
            return database.query("humans", null, null, null, null, null, null);
        }

        void putName(ContentValues values) {
            database.insert("humans", null, values);
        }

        void dropHumansTable() {
            database.delete("humans", null, null);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            database.execSQL("create table if not exists humans (id integer primary key autoincrement, name text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {/*nothing*/}
    }
}
