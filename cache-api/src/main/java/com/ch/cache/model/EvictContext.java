package com.ch.cache.model;

public class EvictContext<K,V> {
    K key;
    V value;
    String type;

    public EvictContext(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public EvictContext() {
    }

    public K key() {
        return key;
    }

    public EvictContext<K,V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public EvictContext<K,V> value(V value) {
        this.value = value;
        return this;
    }

    public String type() {
        return type;
    }

    public EvictContext<K,V> type(String type) {
        this.type = type;
        return this;
    }
}
