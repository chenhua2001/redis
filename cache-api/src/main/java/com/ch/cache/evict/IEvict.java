package com.ch.cache.evict;


import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;
import com.ch.cache.model.IRemoveContext;

public interface IEvict<K,V> {
    K getEvictKey();
    EvictContext<K,V> evict(CacheEvictContext<K,V> context);
    void update(K key);
    void removeKey(K key);
}
