package com.ch.cache.model;

import com.ch.cache.core.ICache;
import com.ch.cache.inteceptor.Intercept;

import java.lang.reflect.Method;

public interface ICacheInterceptorContext<K,V> {
    public Method method();

    public Intercept intercept();

    public Object[] args();


    public ICache<K,V> cache();
}
