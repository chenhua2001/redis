package com.ch.cache.struct;

import java.util.HashMap;

public class ILinkedHashMap<K> {
    KeyNode<K> head;
    KeyNode<K> tail;
    HashMap<K,KeyNode<K>> hashMap;

    public ILinkedHashMap() {
        head=new KeyNode<>();
        tail=head;
        hashMap=new HashMap<>();
    }
    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }
    public void remove(K key){
        KeyNode<K> node = hashMap.remove(key);
        if(node==null) return;
        KeyNode<K> next=node.next;
        KeyNode<K> pre=node.pre;
        pre.next=next;
        next.pre=pre;
        node=null;//help gc
    }
    public void add(K key){
        remove(key);
        KeyNode<K> node=new KeyNode<K>().key(key);
        //在hashMap中添加
        hashMap.put(key,node);
        //连成链表
        tail.next=node;
        node.pre=tail;
        tail=node;
    }
    public K getEldestKey(){
        return head.next.key;
    }

}
