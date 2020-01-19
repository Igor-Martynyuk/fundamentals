package com.itea.practice.fundamentals.task.components.data.provider;

import android.net.Uri;

public class PingHistoryUriBuilder {

    private static Uri.Builder getBuilder() {
        return new Uri.Builder()
                .scheme(PingHistoryProvider.SCHEME)
                .authority(PingHistoryProvider.AUTHORITY);
    }

    public static Uri build() {
        return getBuilder().build();
    }

    public static Uri build(int index) {
        return getBuilder().appendPath(String.valueOf(index)).build();
    }

}
