package com.ch.cache.expire;

import com.ch.cache.core.ICache;

import java.util.concurrent.*;

public class ScheduleRemove {
    ScheduleCheck expire;
    ICache cache;
    public ScheduleRemove(ScheduleCheck expire,ICache cache) {
        this.expire = expire;
        this.cache=cache;
        scheduleRemove();
    }

    public void scheduleRemove() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(expire.type());
                return thread;
            }
        });
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                expire.check(cache);
            }
        },expire.initDelay(),expire.period(),expire.timeUnit());
    }

}
