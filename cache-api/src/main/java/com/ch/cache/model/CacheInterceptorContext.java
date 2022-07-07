package com.ch.cache.model;

import com.ch.cache.core.ICache;
import com.ch.cache.inteceptor.Intercept;

import java.lang.reflect.Method;

public class CacheInterceptorContext<K,V> implements ICacheInterceptorContext<K,V>{
    private Method method;
    private Object[] args;
    private ICache<K,V> cache;
    private Intercept intercept;

    @Override
    public Method method() {
        return method;
    }


    public CacheInterceptorContext<K,V> method(Method method) {
        this.method = method;
        this.intercept=method.getAnnotation(Intercept.class);
        return this;
    }

    @Override
    public Object[] args() {
        return args;
    }

    public CacheInterceptorContext<K,V> args(Object[] args) {
        this.args = args;
        return this;
    }

    public ICache<K, V> cache() {
        return cache;
    }

    public CacheInterceptorContext<K,V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public Intercept intercept() {
        return intercept;
    }
}
