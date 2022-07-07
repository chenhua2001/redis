package com.ch.cache.listener;

import com.ch.cache.model.ISlowListenContext;

public interface ISlowListener {
    void listen(ISlowListenContext context);
    long slowTimeLimited();
}
