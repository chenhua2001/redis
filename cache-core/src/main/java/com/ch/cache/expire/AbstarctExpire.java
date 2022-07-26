package com.ch.cache.expire;

import com.ch.cache.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstarctExpire<K> implements IExpire<K> {

    Logger logger= LoggerFactory.getLogger(AbstarctExpire.class);

    //快慢模式
    private static int FASTMODE_SIZE=10;
    private static int SLOWMODE_SIZE=30;
    protected int limitRemoveSize=SLOWMODE_SIZE;
    protected int limitTime=1000*5;
    //key->long
    HashMap<K,Long> expireMap;

    public AbstarctExpire() {
        this.expireMap = new HashMap<>();
    }

    protected void changeToFastMode(){
        logger.info("修改为快模式");
        limitRemoveSize=FASTMODE_SIZE;
    }
    protected void changeToSlowMode(){
        logger.info("修改为慢模式");
        limitRemoveSize=SLOWMODE_SIZE;
    }

    @Override
    public void refresh(Collection<K> keys,ICache cache) {
        //看keyset大还是expireMap的set的大
        Set<K> expireKeys = expireMap.keySet();
        Iterator<K> iterator;
        if(keys.size()>= expireKeys.size()){
            iterator = expireKeys.iterator();
        }
        else {
            iterator = keys.iterator();
        }
        while (iterator.hasNext()){
            K key = iterator.next();
            Long time = expireMap.get(key);
            if(System.currentTimeMillis()>time){
                logger.info("【刷新】：刷新移除了"+key);
                cache.remove(key);
                iterator.remove();
            }
        }
    }

}
