package com.ch.cache.evict;

import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;
import com.ch.cache.struct.FreQueue;

public class LFUEvict<K,V> implements IEvict<K,V>,IGetUpdated{

    FreQueue<K> freQueue=new FreQueue<>();

    @Override
    public K getEvictKey() {
        return freQueue.getEldestKey();
    }

    @Override
    public EvictContext<K, V> evict(CacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        int limitSize = context.limitSize();
        EvictContext<K,V> evictContext=new EvictContext<>();
        if(cache.size()>=limitSize){
            K evictKey = getEvictKey();
            V evictValue = cache.remove(evictKey);
            removeKey(evictKey);
            evictContext.key(evictKey).value(evictValue);
        }
        return evictContext;
    }

    @Override
    public void update(K key) {
        freQueue.add(key);
    }

    @Override
    public void removeKey(K key) {
        freQueue.remove(key);
    }

    @Override
    public String toString() {
        return
                "freQueue=" + freQueue
                ;
    }
}
