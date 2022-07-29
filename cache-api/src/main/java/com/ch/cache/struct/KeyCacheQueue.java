package com.ch.cache.struct;

public interface KeyCacheQueue<K> {
    void add(K key);
    void remove(K key);
    K getEldestKey();
}
