package com.ch.cache.listener;

import com.ch.cache.model.ISlowListenContext;

public class SlowListenerContext implements ISlowListenContext {
    String methodName;
    Object[] args;
    long spendTime;

    public String methodName() {
        return methodName;
    }

    public SlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Object[] args() {
        return args;
    }

    public SlowListenerContext args(Object[] args) {
        this.args = args;
        return this;
    }

    public long spendTime() {
        return spendTime;
    }

    public SlowListenerContext spendTime(long spendTime) {
        this.spendTime = spendTime;
        return this;
    }
}
