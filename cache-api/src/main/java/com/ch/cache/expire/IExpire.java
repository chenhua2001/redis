package com.ch.cache.expire;

import com.ch.cache.core.ICache;

public interface IExpire<K> {
    void expireAt(K key,long time);
    Long expireTime(K key);
}
