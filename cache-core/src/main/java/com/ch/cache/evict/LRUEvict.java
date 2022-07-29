package com.ch.cache.evict;

import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheEvictContext;
import com.ch.cache.model.EvictContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUEvict<K,V> extends LinkedHashMap<K,V> implements IEvict<K,V>,IGetUpdated{
    /*
    * 最久的元素
    * */
    Map.Entry<K,V> eldest;


    boolean evictFlag=false;

    public LRUEvict(){
        super(16,0.75f,true);//令accessOrder为true
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> _eldest) {
        eldest=_eldest;
        return evictFlag;
    }

    @Override
    public K getEvictKey() {
        return eldest.getKey();
    }

    @Override
    public EvictContext<K,V> evict(CacheEvictContext<K,V> context) {
        ICache<K,V> cache=context.cache();
        int limitSize = context.limitSize();
        //如果需要移除（size<=iache.size()）
        EvictContext<K,V> evictContext=null;
        //如果不需要驱除，在我们也已经通过反射来在linkedHashMap进行了添加，这里的put主要是为了调用afterNodeInsert
        if(cache.size()>=limitSize){
            evictFlag=true;
            super.put(context.key(),null);//如果不使用代理，只有当size>=limitSize的时候才会put，这个时候remove删除的是要add的key，而此时add并没有真正执行，所以无效删除
            K evictKey = getEvictKey();
            V evictValue = cache.remove(evictKey);
            evictContext = new EvictContext<K,V>().key(evictKey).value(evictValue);
        }else
            evictFlag=false;
        return evictContext;
    }

    @Override
    public void update(K key) {
        super.put(key,null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }


}
