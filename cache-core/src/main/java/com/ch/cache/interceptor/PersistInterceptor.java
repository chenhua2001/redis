package com.ch.cache.interceptor;

import com.alibaba.fastjson.JSON;
import com.ch.cache.core.ICache;
import com.ch.cache.inteceptor.CacheInterceptor;
import com.ch.cache.model.ICacheInterceptorContext;
import com.ch.cache.model.JsonPersistContext;
import com.ch.cache.persist.IPersist;
import com.ch.cache.persist.JsonPersist;

import java.lang.reflect.Method;

public class PersistInterceptor implements CacheInterceptor {

    @Override
    public void before(ICacheInterceptorContext context) {

    }

    @Override
    public void after(ICacheInterceptorContext context) {
        IPersist persist = context.cache().persist();
        if(persist instanceof JsonPersist) {
            JsonPersist jsonPersist=(JsonPersist) persist;
            String method = context.method().getName();
            Object[] args = context.args();
            JsonPersistContext record = new JsonPersistContext();
            record.setMethodName(method);
            record.setArgs(args);
            String jsonRecord = JSON.toJSONString(record);
            jsonPersist.append(jsonRecord);
        }
    }
}
