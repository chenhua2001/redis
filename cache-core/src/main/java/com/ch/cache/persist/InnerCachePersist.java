package com.ch.cache.persist;

import com.ch.cache.core.ICache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class InnerCachePersist {
    private ICache iCache;
    private IPersist iPersist;

    public InnerCachePersist(ICache iCache, IPersist iPersist) {
        this.iCache = iCache;
        this.iPersist = iPersist;
    }

    public void init(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("持久化线程");
                return thread;
            }
        });
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                iPersist.persist(iCache);
            }
        }, iPersist.initDelay(), iPersist.period(),iPersist.timeUnit());
    }


}
