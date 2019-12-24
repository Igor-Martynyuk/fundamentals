package com.itea.practice.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class PingStatsStorage {
    private final String PREFS_FILE_NAME = "ping_stats_prefs";
    private final String PREFS_LOGS_KEY = "ping_logs";

    private Gson gson;
    private SharedPreferences preferences;
    private List<PingLog> logs;

    public PingStatsStorage(Context context) {
        this.gson = new Gson();
        this.preferences = context.getSharedPreferences(this.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        this.logs = this.parseLogs();
    }

    @SuppressWarnings("unchecked")
    private List<PingLog> parseLogs() {
        String json = this.preferences.getString(this.PREFS_LOGS_KEY, "");

        return json.isEmpty()
                ? new ArrayList<PingLog>()
                : (List<PingLog>) this.gson.fromJson(json, new TypeToken<ArrayList<PingLog>>() {/*nothing*/
                }.getType()
        );
    }

    public void insertLog(PingLog log) {
        this.logs.add(log);
        this.preferences.edit().putString(this.PREFS_LOGS_KEY, this.gson.toJson(this.logs)).apply();
    }

    public PingLog getLog(int index) {
        return this.logs.get(index);
    }

    public List<PingLog> getLogs() {
        return this.logs;
    }

    public List<PingLog> filter(PingFilter filter) {
        List<PingLog> result = new ArrayList<>();

        for (PingLog current : logs) {
            if (filter.filter(current)) {
                result.add(current);
            }
        }

        return result;
    }

}
