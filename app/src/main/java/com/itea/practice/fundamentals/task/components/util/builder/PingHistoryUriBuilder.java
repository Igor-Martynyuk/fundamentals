package com.itea.practice.fundamentals.task.components.util.builder;

import android.net.Uri;

import com.itea.practice.fundamentals.Const;

public class PingHistoryUriBuilder {

    private static Uri.Builder getBuilder() {
        return new Uri.Builder()
                .scheme(Const.SCHEME)
                .authority(Const.AUTHORITY)
                .appendPath(Const.SEGMENT_HISTORY);
    }

    public static Uri build() {
        return getBuilder().build();
    }

    public static Uri build(int index) {
        return getBuilder().appendPath(String.valueOf(index)).build();
    }

}
