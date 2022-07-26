package com.ch.cache.core;

import com.ch.cache.evict.IEvict;
import com.ch.cache.expire.IExpire;
import com.ch.cache.listener.IRemoveListener;
import com.ch.cache.listener.ISlowListener;
import com.ch.cache.persist.IPersist;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICache<K,V> {
    void add(K key,V value);
    V remove(K key);
    V get(K key);
    void expireAt(K key,long time);
    void expireAfter(K key,long time);
    Long expireTime(K key);
    int size();
    int limitSize();
    IEvict<K,V> evict();
    IPersist persist();
    Set<Map.Entry<K,V>> entrySet();
    Set<K> keys();
    Collection<V> values();
    ISlowListener slowListener();
    IExpire<K> expire();
}
