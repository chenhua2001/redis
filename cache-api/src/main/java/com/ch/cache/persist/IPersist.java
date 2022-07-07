package com.ch.cache.persist;

import com.ch.cache.core.ICache;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public interface IPersist {
     void persist(ICache iCache);
     long initDelay();
     long period();
     TimeUnit timeUnit();
     void load(ICache iCache) throws InvocationTargetException, IllegalAccessException;
}
