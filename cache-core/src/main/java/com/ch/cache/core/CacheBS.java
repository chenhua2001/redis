package com.ch.cache.core;

import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.inteceptor.Intercept;
import com.ch.cache.interceptor.CacheInterceptors;
import com.ch.cache.interceptor.PersistInterceptor;
import com.ch.cache.interceptor.RefreshInterceptor;
import com.ch.cache.listener.ISlowListener;
import com.ch.cache.listener.SlowListenerContext;
import com.ch.cache.model.CacheInterceptorContext;
import com.ch.cache.model.CacheProxyContext;
import com.ch.cache.model.ICacheInterceptorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CacheBS{

    ICache cache;

    List<CacheInterceptor> evictInterceptor= CacheInterceptors.getEvictInterceptors();

    List<PersistInterceptor> persistInterceptors=CacheInterceptors.getPersistInterceptors();

    List<RefreshInterceptor> refreshInterceptors=CacheInterceptors.getRefreshInterceptors();

    public Object invoke(CacheProxyContext context) throws InvocationTargetException, IllegalAccessException {
        Object[] args = context.args();
        Method method = context.method();
        this.cache=context.iCache();
        CacheInterceptorContext interceptorContext = new CacheInterceptorContext().method(method).args(args).cache(this.cache);
        //慢操作记录开始时间
        long startTime=System.currentTimeMillis();
        //拦截器执行
        preHandler(interceptorContext);
        //执行方法
        Object object= method.invoke(this.cache,args);
        //拦截器执行
        afterHandler(interceptorContext);
        //慢操作监听器监听内存
        long endTime=System.currentTimeMillis();
        long time=endTime-startTime;
        ISlowListener slowListener=cache.slowListener();
        SlowListenerContext slowListenerContext = new SlowListenerContext().methodName(method.getName()).args(args).spendTime(time);
        if(time>=slowListener.slowTimeLimited())
            slowListener.listen(slowListenerContext);
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
            if(intercept.refresh()){
                refreshInterceptors.forEach(refreshInterceptor -> refreshInterceptor.before(context));
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
            if(intercept.refresh()){
                refreshInterceptors.forEach(refreshInterceptor -> refreshInterceptor.after(context));
            }

        }
    }
}
