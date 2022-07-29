package com.ch.cache.interceptor;

import com.ch.cache.core.ICache;
import com.ch.cache.evict.IEvict;
import com.ch.cache.evict.IGetUpdated;
import com.ch.cache.evict.LRUEvict;
import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.model.ICacheInterceptorContext;
import org.omg.PortableInterceptor.Interceptor;

public class EvictInterceptor<K,V> implements CacheInterceptor<K,V> {

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

        ICache cache = context.cache();
        IEvict  evict = cache.evict();
        String methodName = context.method().getName();
        if(methodName.equals("add")) {
            Object key =  context.args()[0];
            evict.update(key);//在这些方法完成之后，更新key的位置
            return;
        }
        if(methodName.equals("remove")) {
            evict.removeKey(context.args()[0]);
            return;
        }
        if(methodName.equals("get")&&evict instanceof IGetUpdated){
            evict.update(context.args()[0]);
        }
    }
}
