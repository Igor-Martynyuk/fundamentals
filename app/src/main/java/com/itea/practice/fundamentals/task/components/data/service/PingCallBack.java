package com.itea.practice.fundamentals.task.components.data.service;

public interface PingCallBack {
    void onSuccess(long started, long finished);

    void onFailure(long started, long finished);
}
