package com.ch.cache.struct;

public class KeyNode<K> {
    K key;
    KeyNode<K> pre;
    KeyNode<K> next;

    public K key() {
        return key;
    }

    public KeyNode<K> key(K key) {
        this.key = key;
        return this;
    }

    public KeyNode<K> getPre() {
        return pre;
    }

    public KeyNode<K> setPre(KeyNode<K> pre) {
        this.pre = pre;
        return this;
    }

    public KeyNode<K> getNext() {
        return next;
    }

    public KeyNode<K> setNext(KeyNode<K> next) {
        this.next = next;
        return this;
    }
}
