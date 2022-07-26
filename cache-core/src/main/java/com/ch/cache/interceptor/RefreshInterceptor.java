package com.ch.cache.interceptor;

import com.ch.cache.core.ICache;
import com.ch.cache.expire.IExpire;
import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.model.ICacheInterceptorContext;

import java.util.HashSet;

public class RefreshInterceptor implements CacheInterceptor {
    @Override
    public void before(ICacheInterceptorContext context) {
        ICache cache = context.cache();
        IExpire expire = cache.expire();
        String methodName = context.method().getName();
        Object[] args = context.args();
        if(methodName.equals("get")){
            HashSet<Object> objects = new HashSet<>();
            objects.add(args[0]);
            expire.refresh(objects,cache);
        }else expire.refresh(cache.keys(),cache);
    }

    @Override
    public void after(ICacheInterceptorContext context) {

    }
}
