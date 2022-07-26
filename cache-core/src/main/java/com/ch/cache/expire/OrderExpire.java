package com.ch.cache.expire;

import com.ch.cache.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class OrderExpire<K> extends AbstarctExpire<K> implements ScheduleCheck{
    Logger logger= LoggerFactory.getLogger(OrderExpire.class);

    public OrderExpire() {
        super();
//        startExpireThread();//开始周期性删除
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
        return "遍历删除";
    }


    @Override
    public void check(ICache cache) {
        //周期检查是否有过期元素
        int count = 0;
        Iterator<Map.Entry<K, Long>> iterator = expireMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K,Long> entry=iterator.next();
            System.out.println(expireMap.toString());
            long time = entry.getValue();
            logger.info("检查到{}，过期时间为{}，当前时间{}", entry.getKey(), entry.getValue(), System.currentTimeMillis());
            if (time <= System.currentTimeMillis()) {
                K key = entry.getKey();
                iterator.remove();
                cache.remove(key);
                count++;
                logger.info("{}::key={}被删除了，已删除{}个", Thread.currentThread().getName(), key, count);
            }
            if (count == limitRemoveSize) break;
        }

        logger.info("=============单次检查结束");
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
