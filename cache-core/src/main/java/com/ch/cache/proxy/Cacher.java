package com.ch.cache.proxy;

import com.ch.cache.core.Cache;
import com.ch.cache.core.ICache;
import com.ch.cache.evict.IEvict;
import com.ch.cache.evict.LRUEvict;
import com.ch.cache.expire.IExpire;
import com.ch.cache.expire.RadomExpire;
import com.ch.cache.persist.IPersist;
import com.ch.cache.persist.InnerCachePersist;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

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
    private Class<? extends IExpire> expireClass=RadomExpire.class;
    /*
    * 持久化策略
    * */
    private IPersist iPersist;

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

    public Cacher<K,V> expire(Class<? extends IExpire> _expire) throws IllegalAccessException, InstantiationException {
        expireClass=_expire;
        return this;
    }

    public ICache<K,V> build() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Cache<K, V> cache = new Cache<K, V>(limitSize)
                .evict(evict)
                .expire(expireClass)
                .persist(iPersist);

        //会进行周期持久化
        if(iPersist!=null) {
            new InnerCachePersist(cache,iPersist).init();
        }


        //代理工厂生成代理
        return CacheFactory.getProxy(cache);
    }
}
