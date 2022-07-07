package com.ch.cache.model;

import com.ch.cache.core.ICache;

public class CacheEvictContext<K,V> {
    /*
    * 缓存对象
    * */
    ICache<K,V> cache;

    /*

    * 大小限制
    * */
    int limitSize;

    /*
    * 准备要添加的key
    * */
    K key;

    //getter And Setter
    public K key() {
        return key;
    }

    public CacheEvictContext<K,V> key(K key) {
        this.key = key;
        return this;
    }

    public int limitSize() {
        return limitSize;
    }

    public CacheEvictContext<K,V> limitSize(int limitSize) {
        this.limitSize = limitSize;
        return this;
    }

    public ICache<K,V> cache() {
        return cache;
    }

    public CacheEvictContext<K,V> cache(ICache<K,V> cache) {
        this.cache = cache;
        return this;
    }
}
