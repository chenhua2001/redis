package com.ch.cache.expire;

import com.ch.cache.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstarctExpire<K> implements IExpire<K> {
    Logger logger= LoggerFactory.getLogger(AbstarctExpire.class);
    protected ICache<K,?> cache;
    private static int FASTMODE_SIZE=10;
    private static int SLOWMODE_SIZE=30;
    protected int limitRemoveSize=SLOWMODE_SIZE;
    protected int limitTime=1000*5;



    protected void changeToFastMode(){
        logger.info("修改为快模式");
        limitRemoveSize=FASTMODE_SIZE;
    }
    protected void changeToSlowMode(){
        logger.info("修改为慢模式");
        limitRemoveSize=SLOWMODE_SIZE;
    }

    //todo 有没有某一种设计模式可以解决
    @Override
    public void setCache(ICache<K, ?> cache) {
        this.cache=cache;
    }
}
