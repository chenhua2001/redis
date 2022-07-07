package com.ch.cache.expire;

import com.ch.cache.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TimeExpire<K> extends AbstarctExpire<K> implements ScheduleCheck{

    TreeMap<Long, LinkedList<K>> TimeExpireMap;

    Logger logger= LoggerFactory.getLogger(TimeExpire.class);

    public TimeExpire() {
        super();
        this.TimeExpireMap = new TreeMap<>();
//        startScheduleExpire();//开始周期性删除
    }

    @Override
    public void expireAt(K key, long time) {
        LinkedList<K> keys = TimeExpireMap.get(time);
        if(keys==null) keys=new LinkedList<>();
        keys.add(key);
        TimeExpireMap.put(time,keys);
        expireMap.put(key,time);
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    @Override
    public String type() {
        return "时间排序删除";
    }

    @Override
    public void check(ICache cache) {
        logger.info("开始单次删除");
        int count=0;
        Iterator<Map.Entry<Long, LinkedList<K>>> iterator = TimeExpireMap.entrySet().iterator();
        retry:
        while (iterator.hasNext()) {
            Map.Entry<Long, LinkedList<K>> entry = iterator.next();
            long time = entry.getKey();
            LinkedList<K> keys = entry.getValue();
            if (time <= System.currentTimeMillis()) {
                for (K key : keys) {
                    cache.remove(key);
                    count++;
                    logger.info("{}-key={}", Thread.currentThread().getName(), key);
                    if (count >= limitRemoveSize) {
                        logger.info("已经超出限制");
                        break retry;
                    }
                }
                iterator.remove();
            }
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
