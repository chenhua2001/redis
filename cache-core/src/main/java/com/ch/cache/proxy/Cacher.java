package com.ch.cache.proxy;

import com.ch.cache.core.Cache;
import com.ch.cache.core.ICache;
import com.ch.cache.evict.IEvict;
import com.ch.cache.evict.LRUEvict;
import com.ch.cache.expire.IExpire;
import com.ch.cache.expire.RadomExpire;
import com.ch.cache.expire.ScheduleCheck;
import com.ch.cache.expire.ScheduleRemove;
import com.ch.cache.listener.IRemoveListener;
import com.ch.cache.listener.ISlowListener;
import com.ch.cache.listener.SlowListener;
import com.ch.cache.persist.IPersist;
import com.ch.cache.persist.InnerCachePersist;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cacher<K,V> {
    /*
     * 大小限制
     * */
    private int limitSize;
    /*
     * 驱除策略
     * */
    private IEvict<K,V> evict=new LRUEvict<>();
    /*
     * 过期key移除策略
     * */
    private ScheduleCheck expire=new RadomExpire<>();
    /*
    * 持久化策略
    * */
    private IPersist iPersist;
    /*
    * 移除监听器
    * */
    private List<IRemoveListener> removeListeners=new ArrayList<>();
    /*
    * 慢操作监听器
    * */
    private ISlowListener slowListener=new SlowListener();

    public Cacher<K,V> evict(IEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    public Cacher() {
    }

    public Cacher<K,V> limitSize(int limitSize){
        this.limitSize=limitSize;
        return this;
    }

    public Cacher<K,V> iPersist(IPersist iPersist) {
        this.iPersist = iPersist;
        return this;
    }

    public Cacher<K,V> expire(ScheduleCheck _expire) throws IllegalAccessException, InstantiationException {
        expire=_expire;
        return this;
    }

    public Cacher<K,V> removeListener(IRemoveListener removeListener) {
        this.removeListeners.add(removeListener);
        return this;
    }

    public Cacher<K,V> slowListener(ISlowListener slowListener) {
        this.slowListener = slowListener;
        return this;
    }

    public ICache<K,V> build() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Cache<K, V> cache = new Cache<K, V>(limitSize)
                .evict(evict)
                .expire((IExpire<K>) expire)
                .persist(iPersist)
                .slowListener(slowListener);

        //会进行周期持久化
        if(iPersist!=null) {
            new InnerCachePersist(cache,iPersist).init();
        }




        if(!removeListeners.isEmpty()){
            cache.removeListener(removeListeners);
        }

        //代理工厂生成代理
        ICache<K, V> proxy = CacheFactory.getProxy(cache);

        if(expire!=null)
            new ScheduleRemove(expire,proxy);
        return proxy;
    }
}
