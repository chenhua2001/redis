package com.ch.cache.struct;

public class FreNodeList<K> {
    private FreNode<K> head;
    private FreNode<K> tail;

    public FreNodeList() {
        head=new FreNode<>();
        tail=head;
    }

    public void add(FreNode<K> node){
        tail.next=node;
        node.pre=tail;
        tail=node;
    }

    public boolean isEmpty(){
        return tail==head;
    }

    public boolean removeAndReturnIfEmpty(FreNode<K> node){
        if(node==null) return false;
        FreNode<K> pre = node.pre;
        FreNode<K> next = node.next;
        pre.next=next;
        if(next!=null)
            next.pre=pre;
        if(tail==node) tail=pre;
        return pre == head && tail == head;
    }

    public FreNode<K> head() {
        return head;
    }

    @Override
    public String toString() {
        return "FreNodeList{" +
                "first=" + head.next +
                ", tail=" + tail +
                '}';
    }
}
