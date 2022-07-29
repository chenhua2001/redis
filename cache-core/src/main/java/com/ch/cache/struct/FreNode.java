package com.ch.cache.struct;

public class FreNode<K> {
    public int fre=0;
    public K key;
    public FreNode<K> pre;
    public FreNode<K> next;

    public FreNode() {
    }

    public FreNode(K key) {
        this.key = key;
    }

    public FreNode<K> fre(int fre) {
        this.fre = fre;
        return this;
    }

    public FreNode<K> key(K key) {
        this.key = key;
        return this;
    }

    public FreNode<K> pre(FreNode<K> pre) {
        this.pre = pre;
        return this;
    }

    public FreNode<K> next(FreNode<K> next) {
        this.next = next;
        return this;
    }

    @Override
    public String toString() {
        return "FreNode{" +
                "key=" + key +
                '}';
    }
}
