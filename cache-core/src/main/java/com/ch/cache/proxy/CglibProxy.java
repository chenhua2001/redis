package com.ch.cache.proxy;

import com.ch.cache.core.CacheBS;
import com.ch.cache.core.ICache;
import com.ch.cache.model.CacheProxyContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements IProxy, MethodInterceptor {
    ICache cache;

    public CglibProxy(ICache cache) {
        this.cache = cache;
    }

    @Override
    public ICache proxy() {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(cache.getClass());
        enhancer.setCallback(this);
        return (ICache) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        CacheProxyContext context=new CacheProxyContext().iCache(cache).method(method).args(objects);
        return new CacheBS().invoke(context);
    }
}
