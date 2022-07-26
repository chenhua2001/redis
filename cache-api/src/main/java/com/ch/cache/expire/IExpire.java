package com.ch.cache.expire;

import com.ch.cache.core.ICache;

import java.util.Collection;

public interface IExpire<K> {
    void expireAt(K key,long time);
    Long expireTime(K key);
    void refresh(Collection<K> keys,ICache cache);
}
