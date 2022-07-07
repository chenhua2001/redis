package com.ch.cache.model;

public interface ISlowListenContext {
    String methodName();
    Object[] args();
    long spendTime();
}
