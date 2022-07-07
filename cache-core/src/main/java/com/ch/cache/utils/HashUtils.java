package com.ch.cache.utils;

public class HashUtils {
    public static int getHash(int hashCode,int size){
        return hashCode&size-1;
    }
}
