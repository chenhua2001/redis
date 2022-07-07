package com.ch.cache.persist;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ch.cache.core.Cache;
import com.ch.cache.core.ICache;
import com.ch.cache.inteceptor.Intercept;
import com.ch.cache.model.JsonPersistContext;
import com.github.houbb.heaven.util.io.FileUtil;
import sun.reflect.misc.MethodUtil;

import javax.print.attribute.standard.Media;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
//将方法与参数放到缓冲区中，定期执行持久化到文件
public class JsonPersist implements IPersist{

    public static HashMap<String, Method> methodMap;
    static {
        methodMap=new HashMap<>();
        for (Method method : Cache.class.getMethods()) {
            Intercept annotation = method.getAnnotation(Intercept.class);
            if(annotation!=null && annotation.aof()) methodMap.put(method.getName(),method);
        }
    }

    List<String> list=new LinkedList<>();

    String dbPath;

    public JsonPersist(String dbPath) {
        this.dbPath = dbPath;
    }

    public void append(String record) {
        list.add(record);
    }
    @Override
    public void load(ICache iCache) throws InvocationTargetException, IllegalAccessException {
        List<String> list = FileUtil.readAllLines(dbPath);
        for (String record : list) {
            JsonPersistContext context = JSON.parseObject(record, JsonPersistContext.class);
            String methodName = context.getMethodName();
            Object[] args = context.getArgs();
            Method method = methodMap.get(methodName);
            MethodUtil.invoke(method,iCache,args);
        }
    }

    @Override
    public void persist(ICache cache) {
        if (!FileUtil.exists(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        FileUtil.append(dbPath,list);
        list.clear();
    }

    @Override
    public long initDelay() {
        return 0;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
