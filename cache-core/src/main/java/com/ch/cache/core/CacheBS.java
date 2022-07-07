package com.ch.cache.core;

import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.inteceptor.Intercept;
import com.ch.cache.interceptor.CacheInterceptors;
import com.ch.cache.interceptor.EvictInterceptor;
import com.ch.cache.interceptor.PersistInterceptor;
import com.ch.cache.model.CacheInterceptorContext;
import com.ch.cache.model.CacheProxyContext;
import com.ch.cache.model.ICacheInterceptorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class CacheBS{

    ICache cache;

    List<CacheInterceptor> evictInterceptor= CacheInterceptors.getEvictInterceptors();

    List<PersistInterceptor> persistInterceptors=CacheInterceptors.getPersistInterceptors();


    public Object invoke(CacheProxyContext context) throws InvocationTargetException, IllegalAccessException {
        Object[] args = context.args();
        Method method = context.method();
        this.cache=context.iCache();
        CacheInterceptorContext interceptorContext = new CacheInterceptorContext().method(method).args(args).cache(this.cache);
        //拦截器执行
        preHandler(interceptorContext);
        //执行方法
        Object object= method.invoke(this.cache,args);
        //拦截器执行
        afterHandler(interceptorContext);
        return object;
    }

    private void preHandler(ICacheInterceptorContext context) {
        Intercept intercept;
        if((intercept=context.intercept())!=null) {
            if(intercept.evict()) {
                //evict
                evictInterceptor.forEach(cacheInterceptor -> cacheInterceptor.before(context));
            }
            if(intercept.aof()){
                persistInterceptors.forEach(persistInterceptor -> persistInterceptor.before(context));
            }
        }
    }

    private void afterHandler(ICacheInterceptorContext context) {
        Intercept intercept;
        if((intercept=context.intercept())!=null) {
            if (intercept.evict()) {
                evictInterceptor.forEach(cacheInterceptor -> cacheInterceptor.after(context));
            }
            if(intercept.aof()){
                persistInterceptors.forEach(persistInterceptor -> persistInterceptor.after(context));
            }

        }
    }
}
