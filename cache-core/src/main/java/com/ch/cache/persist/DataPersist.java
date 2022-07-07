package com.ch.cache.persist;

import com.alibaba.fastjson.JSON;
import com.ch.cache.core.ICache;
import com.ch.cache.model.DataPersistContext;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.util.JsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DataPersist implements IPersist{

    String DBPath;

    public DataPersist(String DBPath) {
        this.DBPath = DBPath;
    }

    @Override
    public void persist(ICache cache) {
        if(!FileUtil.exists(DBPath))
            FileUtil.createFile(DBPath);
        FileUtil.truncate(DBPath);
        Set<Map.Entry> set = cache.entrySet();
        for (Map.Entry entry : set) {
            DataPersistContext context = new DataPersistContext();
            context.setKey(entry.getKey());
            context.setValue(entry.getValue());
            Long time = cache.expireTime(entry.getKey());
            if(time!=null)
                context.setTime(time);
            String string = JSON.toJSONString(context);
            FileUtil.append(DBPath,string);
        }

    }

    @Override
    public long initDelay() {
        return 10;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public void load(ICache iCache) throws InvocationTargetException, IllegalAccessException {
        List<String> list = FileUtil.readAllLines(DBPath);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String data = iterator.next();
            if(data.equals("")) continue;
            DataPersistContext context = JSON.parseObject(data, DataPersistContext.class);
            Object key = context.getKey();
            Object value = context.getValue();
            iCache.add(key,value);
            Long time = context.getTime();
            if(time!=null)
                iCache.expireAt(key,time);
        }
    }
}
