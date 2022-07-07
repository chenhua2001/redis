package com.ch.cache.listener;

import com.ch.cache.model.IRemoveContext;

public class RemoveContext implements IRemoveContext {
    private String type;
    private Object key;
    private Object value;

    public RemoveContext type(String type) {
        this.type = type;
        return this;
    }

    public RemoveContext key(Object key) {
        this.key = key;
        return this;
    }

    public RemoveContext value(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public Object key() {
        return key;
    }

    @Override
    public Object value() {
        return value;
    }
}
