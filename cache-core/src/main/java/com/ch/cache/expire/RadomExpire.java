package com.ch.cache.expire;

import com.ch.cache.core.ICache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RadomExpire<K> extends AbstarctExpire<K> implements ScheduleCheck{


    public RadomExpire() {
        super();
    }

    @Override
    public void expireAt(K key, long time) {
        expireMap.put(key,time);
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    @Override
    public String type() {
        return "随机删除";
    }

    @Override
    public void check(ICache cache) {
        long startTime=System.currentTimeMillis();
        int count=0;
        int start=(int)(Math.random()*expireMap.size());
        Iterator<Map.Entry<K, Long>> iterator = expireMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<K, Long> next = iterator.next();
            if(start--<=0){
                K key = next.getKey();
                long time = next.getValue();
                if(System.currentTimeMillis()>=time){
                    iterator.remove();
                    cache.remove(key);
                    logger.info("{}-key={}",Thread.currentThread().getName(),key);
                    count++;
                }
            }
            if(count>=limitRemoveSize){
                changeToSlowMode();
                break;
            }
            if(System.currentTimeMillis()-startTime>=limitTime)
                changeToFastMode();
        }
    }

    @Override
    public long initDelay() {
        return 0;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
