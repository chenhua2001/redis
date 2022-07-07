package com.ch.cache.core;

import com.ch.cache.evict.IEvict;
import com.ch.cache.expire.IExpire;
import com.ch.cache.inteceptor.Intercept;
import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;
import com.ch.cache.persist.IPersist;
import com.ch.cache.proxy.CacheFactory;
import com.ch.cache.proxy.Cacher;
import com.ch.cache.utils.HashUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K,V> implements ICache<K,V>{

    /*
    * 用来存储数据
    * */
    private HashMap<K,V> table;
    /*
    * 大小限制
    * */
    private int limitSize;
    /*
    * 驱除策略
    * */
    private IEvict<K,V> evict;
    /*
    * 过期key移除策略
    * */
    private IExpire<K> expire;
    /*
    * 持久化
    * */
    private IPersist persist;

    public Cache<K,V> evict(IEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    public Cache(int size) {
        table=new HashMap<K,V>((int)(size*1.4));
        this.limitSize = size;
    }
    public Cache(){
        
    }

    public Cache<K,V> expire(Class<? extends IExpire> _expire) throws IllegalAccessException, InstantiationException {
        expire=_expire.newInstance();
        expire.setCache(this);
        return this;
    }

    @Intercept(evict = true,aof = true)
    @Override
    public void add(K key, V value) {

        CacheEvictContext<K,V> context=new CacheEvictContext<K,V>().cache(this).limitSize(limitSize).key(key);
        EvictContext<K, V> evictContext = this.evict.evict(context);
        //todo 淘汰监听器
        table.put(key,value);

    }

    @Override
    @Intercept(aof = true)
    public V remove(K key) {
        return table.remove(key);
    }

    @Intercept(evict = true)
    @Override
    public V get(K key) {
        return table.get(key);
    }

    @Override
    @Intercept(aof = true)
    public void expireAt(K key, long time) {
        //过期key存储
        expire.expireAt(key,time);
    }


    @Override
    //为什么这里不用加注解:因为after的内部是在某个固定时间点，所以我们不能用持久化来保存after的时间
    public void expireAfter(K key, long time) {
        long t = System.currentTimeMillis()+time;
        ICache<K, V> cache = CacheFactory.getProxy(this);
        cache.expireAt(key,t);
    }

    @Override
    public Long expireTime(K key) {
        return expire.expireTime(key);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public int limitSize() {
        return limitSize;
    }

    @Override
    public IEvict<K, V> evict() {
        return evict;
    }


    public Cache<K,V> persist(IPersist persist) {
        this.persist=persist;

        return this;
    }

    @Override
    public IPersist persist() {
        return persist;
    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        return table.entrySet();
    }

    @Override
    public String toString() {
        return "Cache{" +
                "table=" + table +
                ", limitSize=" + limitSize +
                ", evict=" + evict +
                '}';
    }
}
