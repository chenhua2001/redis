package com.ch.cache.map;

import com.ch.cache.utils.HashUtils;

import java.util.*;

public class CHProgressiveReHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {

    /*
    * 默认容量
    * */
    private final static int defaultCapacity=8;
    /*
    * 默认加载量
    * */
    private final static float defaultFactor=0.75f;
    /*
     * 最大最小容量
     * */
    private final int maxCapacity=1<<30;
    private final int minCapacity=1<<1;
    /*
    * 缩容的阈值
    * */
    private static final float minFactor=0.5f;

    /*
    * 是否正在ReHash
    * */
    private static int rehashState=ReHashState.REHASH_FINISHED;

    /*
    * 存储元素的table
    * */
    private List<List<Entry<K,V>>> table;

    /*
    * 如果正在rehash 那么newTable就是新的table
    * */
    private List<List<Entry<K,V>>> newTable;
    /*
    * rehash的index
    * */
    private int rehashIndex=-1;
    /*
    * 实际存储键值对数量
    * */
    private int size;

    /*
    * 加载量
    * */
    private float factor;

    /*
    * 阈值
    * */
    private int threshold;

    /*
    * 容量
    * */
    private int capacity;

    /*
    * 如果正在扩容，新容量
    * */
    private int newCapacity;

    /*
    * 无参构造函数
    * */
    public CHProgressiveReHashMap() {
        this(defaultCapacity,defaultFactor);
    }

    /*
    *
    * */
    public static int hashState;
    /*
    * 用factor和capacity做入参的构造函数
    * */
    public CHProgressiveReHashMap(int _capacity, float _factor) {
        this.factor = _factor;
        this.capacity = tableSizeFor(_capacity);
        this.threshold=(int)(factor*capacity);
        table=new ArrayList<>(capacity);
        initTable(table,capacity);

    }

    private void initTable(List<List<Entry<K,V>>> table,int capacity) {
        for (int i = 0; i < capacity; i++) {
            table.add(new ArrayList<>());
        }
    }

    private int tableSizeFor(int capacity) {
        int cap=minCapacity;
        while (cap<capacity){
            cap<<=1;
        }
        return cap;
    }


    @Override
    public V remove(Object key) {
        ensureCapacity(size-1);
        int hash = key.hashCode();
        int index = HashUtils.getHash(hash, capacity);
        List<Entry<K, V>> list = table.get(index);
        Iterator<Entry<K, V>> iterator = list.iterator();
        while (iterator.hasNext()){
            Entry<K, V> next = iterator.next();
            if(key.equals(next.getKey())){
                iterator.remove();
                size--;
                return next.getValue();
            }
        }
        if(hashState==ReHashState.REHASH_ING){
            index=HashUtils.getHash(hash,newCapacity);
            list=newTable.get(index);
            iterator = list.iterator();
            while (iterator.hasNext()){
                Entry<K, V> next = iterator.next();
                if(key.equals(next.getKey())){
                    iterator.remove();
                    size--;
                    return next.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V get(Object key) {
        ensureCapacity(size);
        int hash=key.hashCode();
        int index ;
        List<Entry<K,V>> list;
        //如果正在rehash，找新的
        if(rehashState==ReHashState.REHASH_ING){
            index=HashUtils.getHash(hash,newCapacity);
            list=newTable.get(index);
            for (Entry<K, V> entry : list) {
                if(key.equals(entry.getKey()))
                    return entry.getValue();
            }
        }
        //找旧的
        index = HashUtils.getHash(hash, capacity);
        list=table.get(index);
        for (Entry<K, V> entry : list) {
            if(key.equals(entry.getKey()))
                return entry.getValue();
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        ensureCapacity(size+1);//看是否需要扩容
        int curCapacity=capacity;
        List<List<Entry<K,V>>> curTable=table;
        if(rehashState==ReHashState.REHASH_ING){
            curCapacity=newCapacity;
            curTable=newTable;
        }
        int index = HashUtils.getHash(key.hashCode(), curCapacity);
        List<Entry<K, V>> list = curTable.get(index);
        V oldValue=null;
        for (Entry<K, V> entry : list) {
            if(key.equals(entry.getKey())){
                oldValue=entry.getValue();
                entry.setValue(value);
                return oldValue;
            }
        }
        size++;
        return createNewEntry(curTable,index,key,value);
    }


    private V createNewEntry(List<List<Entry<K,V>>> table, int index, K key, V value) {
        List<Entry<K, V>> list = table.get(index);
        list.add(new SimpleEntry<>(key,value));
        return value;
    }

    private void ensureCapacity(int size){
        if(rehashState==ReHashState.REHASH_ING){
            OnceRehash();//进行一次rehash
            return; //正在rehash，不要重复扩容
        }
        if(size>threshold&&(capacity<<1)<=maxCapacity){
            newCapacity=capacity<<1;
        }else if(size>minCapacity&&size<capacity*minFactor){
            newCapacity = tableSizeFor(size);
        }else return;
        rehashState=ReHashState.REHASH_ING;
        newTable=new ArrayList<>(newCapacity);
        initTable(newTable,newCapacity);
        OnceRehash();
    }

    private void OnceRehash() {
        rehashIndex++;
        List<Entry<K,V>> list=table.get(rehashIndex);
        for (Entry<K, V> entry : list) {
            int index = HashUtils.getHash(entry.getKey().hashCode(), newCapacity);
            newTable.get(index).add(entry);
        }
        table.set(rehashIndex,null);
        //要设置为空，不然在移过去之后，oldTable仍然有原来的节点，如果get的时候我们写的是先找旧的，就会发生错误，因为节点被移过去了
        if(rehashIndex==table.size()-1)
            initRehashState();

    }

    private void initRehashState() {
        rehashState=ReHashState.REHASH_FINISHED;
        rehashIndex=-1;
        capacity=newCapacity;
        threshold=(int)(capacity*factor);
        table=newTable;
        newTable=null;
    }

    public int getCapacity(){
        return capacity;
    }

    @Override
    public String toString() {
        return "CHMap{" +
                "maxCapacity=" + maxCapacity +
                ", minCapacity=" + minCapacity +
                ", table=" + table +
                ", newTable="+newTable+
                ", size=" + size +
                ", factor=" + factor +
                ", threshold=" + threshold +
                ", capacity=" + capacity +
                '}';
    }

}
