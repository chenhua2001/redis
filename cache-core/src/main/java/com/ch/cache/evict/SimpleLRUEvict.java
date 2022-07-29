package com.ch.cache.evict;

import com.ch.cache.core.ICache;

import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;
import com.ch.cache.struct.ILinkedHashMap;

public class SimpleLRUEvict<K,V> implements IEvict<K,V>,IGetUpdated{

    ILinkedHashMap<K> linkedHashMap;

    public SimpleLRUEvict() {
        linkedHashMap=new ILinkedHashMap<>();
    }

    @Override
    public K getEvictKey() {
        return linkedHashMap.getEldestKey();
    }

    @Override
    public EvictContext<K,V> evict(CacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        K key = context.key();
        int limitSize = context.limitSize();
        EvictContext<K,V> evictInfo=null;
        if(cache.size()>=limitSize){
            K evictKey = getEvictKey();
            V evictValue = cache.remove(evictKey);
            cache.remove(evictKey);
            linkedHashMap.remove(evictKey);//将linkedHashMap中的元素也移除
            evictInfo=new EvictContext<K,V>().key(evictKey).value(evictValue);
        }
        return evictInfo;
    }

    @Override
    public void update(K key) {
        linkedHashMap.add(key);
    }

    @Override
    public void removeKey(K key) {
        linkedHashMap.remove(key);
    }


}
