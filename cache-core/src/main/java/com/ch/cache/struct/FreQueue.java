package com.ch.cache.struct;

import java.util.HashMap;

public class FreQueue<K> implements KeyCacheQueue<K>{

    private HashMap<Integer,FreNodeList<K>> freMap=new HashMap<>();

    private HashMap<K,FreNode<K>> keyMap=new HashMap<>();

    /*
    * 最小频率
    * */
    private int minFre=1;

    @Override
    public void add(K key) {
        //key已经存在，移出来
        FreNode<K> node = keyMap.get(key);
        if(node!=null){
            FreNodeList<K> nodeList = freMap.get(node.fre);//获取该链表
            if (nodeList.removeAndReturnIfEmpty(node)) {//如果移除之后为空，会返回true
                freMap.remove(node.fre);//将该频率从map中删除
            }

        }
        //key不存在，新建
        else{
            node=new FreNode<>(key);
            keyMap.put(key,node);
            minFre=1;
        }
        // fre+1并放入新链表
        int fre = node.fre + 1;
        node.fre(fre);
        FreNodeList<K> freNodeList = freMap.get(fre);
        if(freNodeList==null) freNodeList=new FreNodeList<>();
        freNodeList.add(node);
        freMap.put(fre,freNodeList);
        changeMinFre();
    }

    @Override
    public void remove(K key) {
        FreNode<K> node = keyMap.remove(key);//从keymap中移除
        if(node!=null) {
            FreNodeList<K> list = freMap.get(node.fre);
            if (list.removeAndReturnIfEmpty(node))
                freMap.remove(node.fre);
            node = null;//help gc
        }
        changeMinFre();
    }

    @Override
    public K getEldestKey() {
        FreNodeList<K> nodeList = freMap.get(minFre);
        if(nodeList==null||nodeList.isEmpty()) throw new RuntimeException("没有元素需要删除");
        return nodeList.head().next.key;
    }

    /*
    * 修改最小频率
    * */
    private void changeMinFre(){
        FreNodeList<K> kFreNodeList = freMap.get(minFre);
        if(kFreNodeList==null||kFreNodeList.isEmpty()){
            while (!freMap.isEmpty()&&(((kFreNodeList=freMap.get(++minFre))==null)||kFreNodeList.isEmpty()));
        }
    }

    @Override
    public String toString() {
        return
                 freMap +"\n"+
                ", minFre=" + minFre
                ;
    }
}
