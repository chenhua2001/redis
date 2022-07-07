package com.ch.cache.expire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RadomExpire<K> extends AbstarctExpire<K>{
    HashMap<K,Long> expireMap;

    public RadomExpire() {
        this.expireMap = new HashMap<>();
        startScheduleExpire();
    }

    @Override
    public void expireAt(K key, long time) {
        expireMap.put(key,time);
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    private void startScheduleExpire(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("随机选取开头进行删除");
                return thread;
            }
        });
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                removeFromRandom();
            }
        },0,1, TimeUnit.SECONDS);
    }

    private void removeFromRandom() {
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
}
