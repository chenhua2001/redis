package com.ch.cache.map;

import com.ch.cache.utils.HashUtils;

import java.util.*;

public class CHMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {
    /*
    * 默认容量
    * */
    private final static int defaultCapacity=8;
    /*
    * 默认加载量
    * */
    private final static float defaultFactor=0.75f;
    /*
     * 最大容量
     * */
    private final int maxCapacity=1<<30;
    private final int minCapacity=1<<1;
    /*
    * 存储元素的table
    * */
    private List<List<Entry<K,V>>> table;
    /*
    * 实际存储键值对数量
    * */
    private int size;

    /*
    * 加载量
    * */
    private float factor;

    /*
    * 缩容的阈值
    * */
    private static final float minFactor=0.5f;
    /*
    *阈值
    * */
    private int threshold;

    /*
    *容量
    * */
    private int capacity;

    /*
    * 无参构造函数
    * */
    public CHMap() {
        this(defaultCapacity,defaultFactor);
    }

    /*
    * 用factor和capacity做入参的构造函数
    * */
    public CHMap(int _capacity,float _factor) {
        this.factor = _factor;
        this.capacity = tableSizeFor(_capacity);
        this.threshold=(int)(factor*capacity);
        table=new ArrayList<>(capacity);
        initTable(table);

    }

    private void initTable(List<List<Entry<K,V>>> table) {
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
        ensureCapacity();
        int index = HashUtils.getHash(key.hashCode(), capacity);
        List<Entry<K, V>> list = table.get(index);
        if(list!=null){
            Iterator<Entry<K, V>> iterator = list.iterator();
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
        int index = HashUtils.getHash(key.hashCode(), capacity);
        List<Entry<K,V>> list=table.get(index);
        for (Entry<K, V> entry : list) {
            if(key.equals(entry.getKey()))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        ensureCapacity();//看是否需要扩容
        int index = HashUtils.getHash(key.hashCode(), capacity);
        List<Entry<K, V>> list = table.get(index);
        V oldValue=null;
        for (Entry<K, V> entry : list) {
            if(key.equals(entry.getKey())){
                oldValue=entry.getValue();
                entry.setValue(value);
                return oldValue;
            }
        }
        size++;
        return createNewEntry(table,index,key,value);
    }


    private V createNewEntry(List<List<Entry<K,V>>> table, int index, K key, V value) {
        List<Entry<K, V>> list = table.get(index);
        list.add(new SimpleEntry<>(key,value));
        return value;
    }

    private void ensureCapacity(){
        if(size>=threshold&&(capacity<<1)<=maxCapacity){
            capacity<<=1;
        }else if(size<=capacity*minFactor){
            capacity = tableSizeFor(size);
        }
        threshold=(int)(capacity*factor);
        rehash(table);
    }

    private void rehash(List<List<Entry<K,V>>> table) {
        ArrayList<List<Entry<K,V>>> newTable=new ArrayList<>(capacity);
        initTable(newTable);
        for (List<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                int index = HashUtils.getHash(entry.getKey().hashCode(), capacity);
                newTable.get(index).add(entry);
            }
        }
        this.table=newTable;
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
                ", size=" + size +
                ", factor=" + factor +
                ", threshold=" + threshold +
                ", capacity=" + capacity +
                '}';
    }

}
