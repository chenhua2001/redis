package com.ch.cache.expire;

import com.ch.cache.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class OrderExpire<K> extends AbstarctExpire<K>{
    Logger logger= LoggerFactory.getLogger(OrderExpire.class);

    HashMap<K,Long> expireMap;

    public OrderExpire() {
        this.expireMap = new HashMap<>();
        startExpireThread();//开始周期性删除
    }



    @Override
    public void expireAt(K key, long time) {
        expireMap.put(key,time);
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }


    private void startExpireThread(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread=new Thread(r);
                thread.setName("根据顺序定期删除过期keys");
                return thread;
            }
        });
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
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
        },0,5, TimeUnit.SECONDS);
    }
}
