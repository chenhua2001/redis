package com.ch.cache.expire;

import com.ch.cache.core.ICache;

import java.util.concurrent.TimeUnit;

public interface ScheduleCheck {
    void check(ICache cache);
    long initDelay();
    long period();
    TimeUnit timeUnit();
    String type();
}
