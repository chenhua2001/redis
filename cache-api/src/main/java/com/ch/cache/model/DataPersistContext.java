package com.ch.cache.model;

public class DataPersistContext {
    Object key;
    Object value;
    Long time;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DataPersistContext{" +
                "key=" + key +
                ", value=" + value +
                ", time=" + time +
                '}';
    }
}
