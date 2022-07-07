package com.ch.cache.proxy;

import com.ch.cache.core.ICache;

import java.lang.reflect.Proxy;

public class CacheFactory{
    public static <K,V> ICache<K,V> getProxy(ICache<K,V> target){
        Class<?> clazz = target.getClass();
        //如果是接口或者是代理类，就使用动态代理
        if(clazz.isInterface()|| Proxy.isProxyClass(clazz)){
            return(ICache<K, V>) new DynamicProxy(target).proxy();
        }
        return (ICache<K, V>) new CglibProxy(target).proxy();
    }
}
