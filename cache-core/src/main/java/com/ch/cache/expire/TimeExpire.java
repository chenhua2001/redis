package com.ch.cache.expire;

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

public class TimeExpire<K> extends AbstarctExpire<K>{

    TreeMap<Long, LinkedList<K>> expireMap;

    Logger logger= LoggerFactory.getLogger(TimeExpire.class);

    public TimeExpire() {
        this.expireMap = new TreeMap<>();
        startScheduleExpire();//开始周期性删除
    }

    @Override
    public void expireAt(K key, long time) {
        LinkedList<K> keys = expireMap.get(time);
        if(keys==null) keys=new LinkedList<>();
        keys.add(key);
        expireMap.put(time,keys);
    }
    //todo:添加key->long
    @Override
    public Long expireTime(K key) {
        return null;
    }

    private void startScheduleExpire(){
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("根据时间排序删除过期key");
                return thread;
            }
        });
        scheduledService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.info("开始单次删除");
                int count=0;
                Iterator<Map.Entry<Long, LinkedList<K>>> iterator = expireMap.entrySet().iterator();
                retry:
                while (iterator.hasNext()){
                    Map.Entry<Long,LinkedList<K>> entry=iterator.next();
                    long time= entry.getKey();
                    LinkedList<K> keys = entry.getValue();
                    if(time<=System.currentTimeMillis()){
                        for (K key : keys) {
                            cache.remove(key);
                            count++;
                            logger.info("{}-key={}",Thread.currentThread().getName(),key);
                            if(count>=limitRemoveSize){
                                logger.info("已经超出限制");
                                break retry;
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        },0,10, TimeUnit.SECONDS);
    }
}
