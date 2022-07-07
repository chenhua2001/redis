package com.ch.cache.struct;

public class OnceTwiceLinkedHashMap<K> {
    /*
    * 访问过一次的key
    * */
    private ILinkedHashMap<K> once;
    /*
    * 访问过两次的key
    * */
    private ILinkedHashMap<K> twice;

    public OnceTwiceLinkedHashMap() {
        once=new ILinkedHashMap<>();
        twice=new ILinkedHashMap<>();
    }

    /*
    * 更新key的位置
    * */
    public void add(K key){
        if(once.containsKey(key)|| twice.containsKey(key)) {
            remove(key);
            twice.add(key);
        }
        else once.add(key);
    }
    /*
    * 从策略队列中删除key
    * */
    public void remove(K key){
        once.remove(key);
        twice.remove(key);
    }
    /*
    * 返回最早元素
    * */
    public K getEldestKey(){
        K key = once.getEldestKey();
        return key==null?twice.getEldestKey():key;
    }
}
