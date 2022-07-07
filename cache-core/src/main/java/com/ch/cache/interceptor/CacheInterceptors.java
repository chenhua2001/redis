package com.ch.cache.interceptor;

import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.persist.IPersist;

import java.util.ArrayList;
import java.util.List;

public class CacheInterceptors {
    public static List<CacheInterceptor> getEvictInterceptors(){
        ArrayList<CacheInterceptor> evictInterceptors = new ArrayList<>();
        evictInterceptors.add(new EvictInterceptor());
        return evictInterceptors;
    }
    public static List<PersistInterceptor> getPersistInterceptors(){
        ArrayList<PersistInterceptor> persistInterceptors=new ArrayList<>();
        persistInterceptors.add(new PersistInterceptor());
        return persistInterceptors;
    }
}
