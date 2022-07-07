package com.ch.cache.expire;

import com.ch.cache.core.ICache;

public interface IExpire<K> {
    void expireAt(K key,long time);
    void setCache(ICache<K,?> cache);
    Long expireTime(K key);
}
