package com.ch.cache.model;

import com.ch.cache.core.ICache;

import java.lang.reflect.Method;

public class CacheProxyContext {
    /*
    * 待执行方法
    * */
    private Method method;
    /*
    * 参数
    * */
    private Object[] args;
    /*
    * cache实体
    * */
    private ICache iCache;

    public CacheProxyContext() {
    }

    public CacheProxyContext(Method method, Object[] args, ICache iCache) {
        this.method = method;
        this.args = args;
        this.iCache = iCache;
    }

    public Method method() {
        return method;
    }

    public CacheProxyContext method(Method method) {
        this.method = method;
        return this;
    }

    public Object[] args() {
        return args;
    }

    public CacheProxyContext args(Object[] args) {
        this.args = args;
        return this;
    }

    public ICache iCache() {
        return iCache;
    }

    public CacheProxyContext iCache(ICache iCache) {
        this.iCache = iCache;
        return this;
    }
}
