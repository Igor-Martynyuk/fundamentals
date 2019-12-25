package com.itea.practice.components;

public interface PingCallBack {
    void onSuccess(long started, long finished);

    void onFailure(long started, long finished);
}
