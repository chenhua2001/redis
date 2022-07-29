package com.ch.cache.evict;

import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;

import java.util.LinkedList;

public class FifoEvict<K,V> implements IEvict<K,V>{
    LinkedList<K> list=new LinkedList<>();
    @Override
    public K getEvictKey() {
        return list.getFirst();
    }

    @Override
    public EvictContext<K, V> evict(CacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        EvictContext<K, V> evictContext = new EvictContext<>();
        if(cache.size()>=context.limitSize()){//是否缓存已经满了
            K evictKey = getEvictKey();
            V val = cache.remove(evictKey);
            list.removeFirst();
            evictContext.value(val).key(evictKey);
        }
        return evictContext;
    }

    @Override
    public void update(K key) {
        list.remove(key);//链表中可能已经存在该key，这一步复杂度为O(N)
        list.add(key);
    }

    @Override
    public void removeKey(K key) {
        list.remove(key);
    }
}
