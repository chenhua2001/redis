package com.ch.cache.model;


import java.io.Serializable;
import java.util.Arrays;

public class JsonPersistContext {

    private String methodName;
    private Object[] args;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "JsonPersistContext{" +
                "methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
