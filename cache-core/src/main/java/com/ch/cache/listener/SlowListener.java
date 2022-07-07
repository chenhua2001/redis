package com.ch.cache.listener;

import com.ch.cache.model.ISlowListenContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowListener implements ISlowListener{
    Logger logger= LoggerFactory.getLogger(SlowListener.class);
    @Override
    public void listen(ISlowListenContext context) {
        logger.warn("[慢操作]：方法->{},参数->{},时间->{}",context.methodName(),context.args(),context.spendTime());
    }

    @Override
    public long slowTimeLimited() {
        return 10;
    }
}
