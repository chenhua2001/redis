package com.ch.cache.evict;

import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;
import com.ch.cache.struct.OnceTwiceLinkedHashMap;

public class TwiceLRUEvict<K,V> implements IEvict<K,V>{

    OnceTwiceLinkedHashMap<K> map=new OnceTwiceLinkedHashMap<>();

    @Override
    public K getEvictKey() {
        return map.getEldestKey();
    }

    @Override
    public EvictContext<K, V> evict(CacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        K key = context.key();
        int limitSize = context.limitSize();
        EvictContext<K,V> evictContext=null;
        if(cache.size()>=limitSize){
            K evictKey = getEvictKey();
            V evictValue = cache.remove(evictKey);
            map.remove(evictKey);
            evictContext=new EvictContext<>(evictKey,evictValue);
        }
        return evictContext;
    }

    @Override
    public void update(K key) {
        map.add(key);
    }

    @Override
    public void removeKey(K key) {
        map.remove(key);
    }
}
