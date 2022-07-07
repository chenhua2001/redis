package com.ch.cache.inteceptor;

import com.ch.cache.model.ICacheInterceptorContext;

public interface CacheInterceptor<K,V> {
    public void before(ICacheInterceptorContext<K,V> context);
    public void after(ICacheInterceptorContext<K,V> context);
}
