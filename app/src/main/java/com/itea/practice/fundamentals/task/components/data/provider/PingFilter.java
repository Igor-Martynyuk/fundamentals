package com.itea.practice.fundamentals.task.components.data.provider;

import com.itea.practice.fundamentals.task.components.data.PingLog;

public interface PingFilter {
    boolean filter(PingLog current);
}
