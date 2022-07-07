package com.ch.cache.proxy;

import com.ch.cache.core.CacheBS;
import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheProxyContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements IProxy, InvocationHandler {

    ICache cache;

    public DynamicProxy(ICache cache) {
        this.cache = cache;
    }

    @Override
    public ICache proxy() {
        Class<? extends ICache> clazz = cache.getClass();
        return (ICache) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CacheProxyContext context = new CacheProxyContext().method(method).args(args).iCache(cache);
        return new CacheBS().invoke(context);
    }
}
