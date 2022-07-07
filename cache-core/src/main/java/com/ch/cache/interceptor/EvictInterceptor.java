package com.ch.cache.interceptor;

import com.ch.cache.core.ICache;
import com.ch.cache.evict.IEvict;
import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.model.ICacheInterceptorContext;
import org.omg.PortableInterceptor.Interceptor;

public class EvictInterceptor<K,V> implements CacheInterceptor<K,V> {

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

        ICache<K,V> cache = context.cache();
        IEvict<K,V> evict = cache.evict();
        if(context.method().getName().equals("add")) {
            K key = (K) context.args()[0];
            evict.update(key);//在这些方法完成之后，更新key的位置
        }
    }
}
